import numpy as np
import pandas as pd
from keras.layers import Dense, Dropout
from keras.models import Sequential
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from keras.regularizers import l2
from sklearn.utils import class_weight
from keras.callbacks import EarlyStopping, ReduceLROnPlateau
import joblib
import warnings

warnings.filterwarnings('ignore') 


def create_model_and_train(dataset: str):
    df = pd.read_csv(dataset)

    X = df.iloc[:, :-1]
    y = df.iloc[:, -1]

    le = LabelEncoder()
    y_encoded = le.fit_transform(y)

    categorical_cols = X.select_dtypes(include=["object", "bool"]).columns

    X_processed = pd.get_dummies(X, columns=categorical_cols, drop_first=True)

    joblib.dump(X_processed.columns.tolist(), 'model_store/model_features.pkl')

    X_train, X_test, y_train, y_test = train_test_split(
        X_processed, y_encoded, test_size=0.10, random_state=1, stratify=y_encoded
    )

    class_weights = class_weight.compute_class_weight(
        class_weight='balanced',
        classes=np.unique(y_train),
        y=y_train
    )
    class_weight_dict = dict(enumerate(class_weights))
    
    scaler = MinMaxScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    joblib.dump(scaler, 'model_store/minmax_scaler.pkl')

    input_features = X_train_scaled.shape[1]

    early_stopping = EarlyStopping(
        monitor='val_loss',
        patience=20,
        restore_best_weights=True
    )
    lr_scheduler = ReduceLROnPlateau(
        monitor='val_loss',
        factor=0.2,
        patience=10,
        min_lr=0.0001
    )

    model = Sequential()
    model.add(Dense(128, activation="relu", input_shape=(input_features,), kernel_regularizer=l2(0.001)))
    model.add(Dropout(0.3))
    model.add(Dense(64, activation="relu", kernel_regularizer=l2(0.001)))
    model.add(Dropout(0.3))
    model.add(Dense(1, activation="sigmoid"))

    model.compile(loss="binary_crossentropy", metrics=["accuracy"])

    model.fit(
        X_train_scaled,
        y_train,
        batch_size=32,
        epochs=200, # Increased epochs, relying on EarlyStopping
        validation_data=(X_test_scaled, y_test),
        callbacks=[early_stopping, lr_scheduler],
        class_weight=class_weight_dict
    )

    loss, accuracy = model.evaluate(X_test_scaled, y_test, verbose=0)

    y_pred_probs = model.predict(X_train_scaled, verbose=0)
    np.set_printoptions(precision=6, suppress=True)

    y_pred_binary = (y_pred_probs > 0.5).astype(int)
    count_ones = y_pred_probs[y_pred_binary == 1]
    count_zeros = y_pred_probs[y_pred_binary == 0]

    return model, loss, accuracy


def main():
    model, loss, accuracy = create_model_and_train("data/final_data.csv")
    print(f"Test Accuracy: {accuracy}")
    print(f"Test Loss: {loss}")
    model.save('model_store/lifestylemodel.keras')


if __name__ == "__main__":
    main()
