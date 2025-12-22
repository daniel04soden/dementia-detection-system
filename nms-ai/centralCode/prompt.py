import keras
import pandas as pd
from sklearn.preprocessing import LabelEncoder
import joblib
import json
import numpy as np

GLOBAL_MODEL = None
GLOBAL_SCALER = None
GLOBAL_FEATURES = None
GLOBAL_LE = LabelEncoder()


def create_quest_dict(
    Diabetic: int,
    AlcoholLevel: float,
    HeartRate: int,
    BloodOxygen: float,
    BodyTemperature: float,
    Weight: float,
    MRI_Delay: float,
    Age: int,
    Dominant_Hand: int,
    Gender: int,
    Family_History: int,
    Smoked: int,
    Dementia_gene: int,
    Physical_Activity: str,
    Depression_Status: int,
    Cognitive_Test_Scores: int,
    Medication_History: int,
    Nutrition_Diet: str,
    Sleep_Quality: int,
    Chronic_Health_Conditions: str,
    Cumulative_Primary: str,
    Cumulative_Secondary: str,
    Cumulative_Degree: str,
):
    return {
        "Diabetic": Diabetic,
        "AlcoholLevel": AlcoholLevel,
        "HeartRate": HeartRate,
        "BloodOxygen": BloodOxygen,
        "BodyTemperature": BodyTemperature,
        "Weight": Weight,
        "MRI_Delay": MRI_Delay,
        "Age": Age,
        "Dominant_Hand": Dominant_Hand,
        "Gender": Gender,
        "Family_History": Family_History,
        "Smoked": Smoked,
        "Dementia_gene": Dementia_gene,
        "Physical_Activity": Physical_Activity,
        "Depression_Status": Depression_Status,
        "Cognitive_Test_Scores": Cognitive_Test_Scores,
        "Medication_History": Medication_History,
        "Nutrition_Diet": Nutrition_Diet,
        "Sleep_Quality": Sleep_Quality,
        "Chronic_Health_Conditions": Chronic_Health_Conditions,
        "Cumulative_Primary": Cumulative_Primary,
        "Cumulative_Secondary": Cumulative_Secondary,
        "Cumulative_Degree": Cumulative_Degree,
    }


def get_int_input(prompt):
    while True:
        try:
            return int(input(prompt))
        except ValueError:
            print("Invalid input. Please enter a whole number.")


def get_float_input(prompt):
    while True:
        try:
            return float(input(prompt))
        except ValueError:
            print("Invalid input. Please enter a number.")


def get_binary_input(prompt):
    while True:
        try:
            value = int(input(prompt))
            if value in [0, 1]:
                return value
            else:
                print("Invalid input. Please enter 1 or 0.")
        except ValueError:
            print("Invalid input. Please enter 1 or 0.")


def ask_user() -> dict:
    print("\n--- Welcome to the Dementia Detection Questionnaire ---")
    print("Please answer the following questions.")
    db = get_binary_input("Diabetic (1=Yes, 0=No)")
    al = get_float_input("Alcohol level")
    hr = get_int_input("Heart Rate: ")
    bo = get_float_input("Blood Oxygen Level: ")
    bt = get_float_input("Body Temperature: ")
    w = get_float_input("Weight: ")
    mri_d = get_float_input("MRI Delay: ")
    age = get_int_input("Age: ")
    dh = get_binary_input("Dominant Hand (1=Right, 0=Left): ")
    gen = get_binary_input("Gender (1=Male, 0=Female): ")
    fh = get_binary_input("Family History of Dementia (1=Yes, 0=No): ")
    sm = get_binary_input("Smoked in their life (1=Yes, 0=No): ")
    dementia_gene = get_binary_input("Dementia Gene (1=Has it, 0=No): ")
    dep_s = get_binary_input("Depression Status (1=Yes, 0=No): ")
    med_h = get_binary_input("Medication History (1=Yes, 0=No): ")
    sq = get_binary_input("Sleep Quality (1=Quality, 0=Poor): ")
    pa = input("Physical Activity Level (e.g., Sedentary, Moderate, High): ")

    while True:
        cts = get_int_input("Cognitive Test Score (0-10): ")
        if 0 <= cts <= 10:
            break
        else:
            print("Score must be between 0 and 10.")

    valid_diet = ["Low-Carb Diet", "Mediterranean Diet", "Balanced Diet"]
    while True:
        nd = input(
            "Nutrition/Diet Type: (eg Low-Carb Diet, Mediterranean Diet, Balanced Diet): "
        ).title()
        if nd in valid_diet:
            break
        else:
            print("Please enter one of the following")
            print(valid_diet)

    valid_health = ["Diabetes", "Hypertension", "Heart Disease", "N/A"]
    while True:
        chc = input(
            "Chronic Health Conditions (e.g., Diabetes, Hypertension, Heart Disease ,or N/A): "
        ).title()
        if chc in valid_health:
            break
        else:
            print("Please enter one of the following")
            print(valid_health)

    valid_edu = ["TRUE", "FALSE"]
    while True:
        cp = input("Cumulative Primary Education (TRUE or FALSE): ").upper()
        if cp in valid_edu:
            break
        else:
            print("Please enter TRUE or FALSE.")

    while True:
        cs = input("Cumulative Secondary Education (TRUE or FALSE): ").upper()
        if cs in valid_edu:
            break
        else:
            print("Please enter TRUE or FALSE.")

    while True:
        cd = input("Cumulative Degree/Higher Education (TRUE or FALSE): ").upper()
        if cd in valid_edu:
            break
        else:
            print("Please enter TRUE or FALSE.")

    # Removed 'Dementia_Status' input field

    return create_quest_dict(
        Diabetic=db,
        AlcoholLevel=al,
        HeartRate=hr,
        BloodOxygen=bo,
        BodyTemperature=bt,
        Weight=w,
        MRI_Delay=mri_d,
        Age=age,
        Dominant_Hand=dh,
        Gender=gen,
        Family_History=fh,
        Smoked=sm,
        Dementia_gene=dementia_gene,
        Physical_Activity=pa,
        Depression_Status=dep_s,
        Cognitive_Test_Scores=cts,
        Medication_History=med_h,
        Nutrition_Diet=nd,
        Sleep_Quality=sq,
        Chronic_Health_Conditions=chc,
        Cumulative_Primary=cp,
        Cumulative_Secondary=cs,
        Cumulative_Degree=cd,
    )


def run_test(answers: list, loaded_model, scaler, model_features, global_le) -> str:
    data_frame_q = pd.DataFrame(answers)

    X = data_frame_q

    categorical_cols = X.select_dtypes(include=["object", "bool"]).columns
    X_processed = pd.get_dummies(X, columns=categorical_cols, drop_first=True)

    X_aligned = X_processed.reindex(columns=model_features, fill_value=0)

    X_scaled = scaler.transform(X_aligned)

    y_pred_probs = loaded_model.predict(X_scaled, verbose=0)

    prediction_probability = y_pred_probs.flatten()[0]

    percentage_likelihood = prediction_probability * 100

    y_pred_binary = (y_pred_probs > 0.5).astype(int)
    predicted_label = global_le.inverse_transform(y_pred_binary.flatten())

    print(f"\n--- Prediction Results ---")
    print(f"Predicted Likelihood of Dementia: {percentage_likelihood:.2f}%")
    print(f"Predicted Class (Decoded): {predicted_label[0]}")

    return f"{percentage_likelihood:.2f}"


def main():
    sample_str_1 = """{
        "answers":[
            {
                "Diabetic": 1, 
                "AlcoholLevel": 0.15, 
                "HeartRate": 95, 
                "BloodOxygen": 92.5, 
                "BodyTemperature": 37.5, 
                "Weight": 95.0, 
                "MRI_Delay": 1.5, 
                "Age": 85, 
                "Dominant_Hand": 1, 
                "Gender": 1, 
                "Family_History": 1, 
                "Smoked": 1, 
                "Dementia_gene": 1, 
                "Physical_Activity": "Sedentary", 
                "Depression_Status": 1, 
                "Cognitive_Test_Scores": 2, 
                "Medication_History": 1, 
                "Nutrition_Diet": "Low-Carb Diet", 
                "Sleep_Quality": 0, 
                "Chronic_Health_Conditions": "Diabetes", 
                "Cumulative_Primary": "TRUE", 
                "Cumulative_Secondary": "TRUE", 
                "Cumulative_Degree": "TRUE"
            }
        ]
    }"""

    sample_str_2 = """{
        "answers":[
            {
                "Diabetic": 0, 
                "AlcoholLevel": 0.0, 
                "HeartRate": 60, 
                "BloodOxygen": 98.5, 
                "BodyTemperature": 36.5, 
                "Weight": 65.0, 
                "MRI_Delay": 55.0, 
                "Age": 60, 
                "Dominant_Hand": 0, 
                "Gender": 0, 
                "Family_History": 0, 
                "Smoked": 0, 
                "Dementia_gene": 0, 
                "Physical_Activity": "Moderate Activity", 
                "Depression_Status": 0, 
                "Cognitive_Test_Scores": 10, 
                "Medication_History": 0, 
                "Nutrition_Diet": "Mediterranean Diet", 
                "Sleep_Quality": 1, 
                "Chronic_Health_Conditions": "N/A", 
                "Cumulative_Primary": "FALSE", 
                "Cumulative_Secondary": "FALSE", 
                "Cumulative_Degree": "FALSE"
            }
        ]
    }"""

    sample_str_3 = """{
        "answers":[
            {
                "Diabetic": 1, 
                "AlcoholLevel": 0.08, 
                "HeartRate": 75, 
                "BloodOxygen": 96.0, 
                "BodyTemperature": 37.0, 
                "Weight": 80.0, 
                "MRI_Delay": 25.0, 
                "Age": 70, 
                "Dominant_Hand": 1, 
                "Gender": 1, 
                "Family_History": 1, 
                "Smoked": 0, 
                "Dementia_gene": 1, 
                "Physical_Activity": "Mild Activity", 
                "Depression_Status": 0, 
                "Cognitive_Test_Scores": 7, 
                "Medication_History": 1, 
                "Nutrition_Diet": "Balanced Diet", 
                "Sleep_Quality": 1, 
                "Chronic_Health_Conditions": "Hypertension", 
                "Cumulative_Primary": "TRUE", 
                "Cumulative_Secondary": "FALSE", 
                "Cumulative_Degree": "FALSE"
            }
        ]
    }"""

    sample_str_4 = """{
        "answers":[
            {
                "Diabetic": 0, 
                "AlcoholLevel": 0.05, 
                "HeartRate": 80, 
                "BloodOxygen": 97.5, 
                "BodyTemperature": 36.8, 
                "Weight": 70.0, 
                "MRI_Delay": 40.0, 
                "Age": 65, 
                "Dominant_Hand": 0, 
                "Gender": 0, 
                "Family_History": 0, 
                "Smoked": 1, 
                "Dementia_gene": 0, 
                "Physical_Activity": "Moderate Activity", 
                "Depression_Status": 0, 
                "Cognitive_Test_Scores": 9, 
                "Medication_History": 0, 
                "Nutrition_Diet": "Mediterranean Diet", 
                "Sleep_Quality": 0, 
                "Chronic_Health_Conditions": "N/A", 
                "Cumulative_Primary": "FALSE", 
                "Cumulative_Secondary": "FALSE", 
                "Cumulative_Degree": "FALSE"
            }
        ]
    }"""

    try:
        GLOBAL_MODEL = keras.models.load_model("model_store/lifestylemodel.keras")
        GLOBAL_SCALER = joblib.load("model_store/minmax_scaler.pkl")
        GLOBAL_FEATURES = joblib.load("model_store/model_features.pkl")
        GLOBAL_LE.fit(["No Dementia", "Dementia"])
    except Exception as e:
        print(
            f"Warning: Could not load ML assets for CLI test. Skipping evaluation. Error: {e}"
        )
        return
    answers_1 = json.loads(sample_str_1).get("answers")
    answers_2 = json.loads(sample_str_2).get("answers")
    answers_3 = json.loads(sample_str_3).get("answers")
    answers_4 = json.loads(sample_str_4).get("answers")

    prediction_1 = run_test(
        answers_1, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE
    )

    prediction_2 = run_test(
        answers_2, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE
    )

    prediction_3 = run_test(
        answers_3, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE
    )

    prediction_4 = run_test(
        answers_4, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE
    )

    print(prediction_1)
    print(prediction_2)
    print(prediction_3)
    print(prediction_4)


if __name__ == "__main__":
    main()
