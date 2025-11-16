import numpy as np
import pandas as pd
from keras.layers import Dense
from keras.models import Sequential
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder, MinMaxScaler


def create_model_and_train(dataset: str):
    df = pd.read_csv(dataset)
    X = df.iloc[:, :-1]
    y = df.iloc[:, -1]

    X = X.drop(
        columns=["Description", "Current_Medication", "General", "Possible_Data_Loss"],
        errors="ignore",
    )

    le = LabelEncoder()  # Labels relevant - Supervised learning
    y_encoded = le.fit_transform(y)

    categorical_cols = X.select_dtypes(include=["object", "bool"]).columns

    X_processed = pd.get_dummies(X, columns=categorical_cols, drop_first=True)

    X_train, X_test, y_train, y_test = train_test_split(
        X_processed, y_encoded, test_size=0.20, random_state=1, stratify=y_encoded
    )

    scaler = MinMaxScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    input_features = X_train_scaled.shape[1]

    model = Sequential()
    model.add(Dense(64, activation="relu", input_shape=(input_features,)))
    model.add(Dense(64, activation="relu"))
    model.add(Dense(1, activation="sigmoid"))

    model.compile(optimizer="adam", loss="binary_crossentropy", metrics=["accuracy"])

    model.fit(
        X_train_scaled,
        y_train,
        batch_size=32,
        epochs=50,
        validation_data=(X_test_scaled, y_test),
    )

    loss, accuracy = model.evaluate(X_test_scaled, y_test)

    y_pred_probs = model.predict(X_train_scaled)
    y_pred_binary = (y_pred_probs > 0.5).astype(int)

    print(y_pred_binary)

    return model, loss, accuracy


def main():
    model, loss, accuracy = create_model_and_train("data/final_data.csv")
    print(accuracy)
    print(loss)


if __name__ == "__main__":
    main()
