import numpy as np
import pandas as pd
from keras.layers import Dense,Dropout
from keras.models import Sequential
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from keras.layers import Dense
from keras.regularizers import l2
import joblib 


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

    scaler = MinMaxScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    
    joblib.dump(scaler, 'model_store/minmax_scaler.pkl')

    input_features = X_train_scaled.shape[1]

    model = Sequential()
    model.add(Dense(64, activation="relu", input_shape=(input_features,),kernel_regularizer=l2(0.001)))
    model.add(Dropout(0.2))
    model.add(Dense(64, activation="relu",kernel_regularizer=l2(0.001)))
    model.add(Dropout(0.2))
    model.add(Dense(1, activation="sigmoid"))

    
    model.compile(loss="binary_crossentropy", metrics=["accuracy"])

    model.fit(
        X_train_scaled,
        y_train,
        batch_size=32,
        epochs=125,
        validation_data=(X_test_scaled, y_test),
    )

    loss, accuracy = model.evaluate(X_test_scaled, y_test)

    y_pred_probs = model.predict(X_train_scaled)
    y_pred_binary = (y_pred_probs > 0.5).astype(int) # Printing out results
    count_ones = y_pred_binary[y_pred_binary == 1]
    count_zeros = y_pred_binary[y_pred_binary == 0]
    print(f"Number of demented patients in train dataset: {len(count_ones)}")
    print(f"Number of non-demented patients in train dataset: {len(count_zeros)}")

    return model, loss, accuracy


def main():
    model, loss, accuracy = create_model_and_train("data/final_data.csv")
    print(accuracy)
    print(loss)
    model.save('model_store/lifestylemodel.keras')


if __name__ == "__main__":
    main()
