import pandas as pd
import json


def aggregate_new_patient_data(
    json_data: str,
    file_to_add_to: str,
    save: bool,
) -> pd.DataFrame:
    """
    Expects json array derived from user submitted csv file (of if neededed json file)
    that adds new user data.

    param:
        json_data: JSON string containing array of patient records
        file_to_add_to: Path to the CSV file to append to
        save: Whether to save the result back to the CSV file

    return:
        pd.DataFrame: Combined dataframe with original and new data

    format:
        [
            {
                "Diabetic": 1,
                "AlcoholLevel": 0.084973629,
                "HeartRate": 98,
                "BloodOxygenLevel": 96.23,
                "BodyTemperature": 36.22,
                "Weight": 57.6,
                "MRI_Delay": 36.42,
                "Age": 60,
                "Dominant_Hand": 0,
                "Gender": 0,
                "Family_History": 0,
                "Smoked": 1,
                "Dementia_Gene": 0,
                "Physical_Activity": "Sedentary",
                "Depression_Status": 0,
                "Cognitive_Test_Scores": 10,
                "Medication_History": 0,
                "Nutrition_Diet": "Low-Carb Diet",
                "Sleep_Quality": 0,
                "Chronic_Health_Conditions": "Diabetes",
                "Dementia": 0,
                "Cumulative_Primary": 1,
                "Cumulative_Secondary": 0,
                "Cumulative_Degree": 0
            },
            {
                "Diabetic": 1,
                "AlcoholLevel": 0.084973629,
                "HeartRate": 98,
                "BloodOxygenLevel": 96.23,
                "BodyTemperature": 36.22,
                "Weight": 57.6,
                "MRI_Delay": 36.42,
                "Age": 60,
                "Dominant_Hand": 0,
                "Gender": 0,
                "Family_History": 0,
                "Smoked": 1,
                "Dementia_Gene": 0,
                "Physical_Activity": "Sedentary",
                "Depression_Status": 0,
                "Cognitive_Test_Scores": 10,
                "Medication_History": 0,
                "Nutrition_Diet": "Low-Carb Diet",
                "Sleep_Quality": 0,
                "Chronic_Health_Conditions": "Diabetes",
                "Dementia": 0,
                "Cumulative_Primary": 1,
                "Cumulative_Secondary": 0,
                "Cumulative_Degree": 0
            }
        ]
    """

    data = json.loads(json_data)

    new_df = pd.DataFrame(data)
    original_df = pd.read_csv(file_to_add_to)
    new_df = new_df[original_df.columns]

    combined_df = pd.concat([original_df, new_df], ignore_index=True)
    if save:
        combined_df.to_csv(file_to_add_to, index=False)
        print(f"Successfully appended {len(new_df)} record(s) to {file_to_add_to}")
        print(f"Total records: {len(combined_df)}")

    return combined_df
