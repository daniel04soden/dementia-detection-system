import os

import pandas as pd
pd.set_option("display.max_rows", None)


def count_dementia(df: pd.DataFrame, has: int) -> int:
    demented_patients = df[df["Dementia"] == has]
    return len(demented_patients)


def replace_column_values(file_name: str) -> pd.DataFrame:
    replaced_data = pd.read_csv(file_name)
    replaced_data["Weight"] = replaced_data["Weight"].round(1)

    replaced_data["Depression_Status"] = replaced_data["Depression_Status"].replace(
        {"Yes": 1, "No": 0}
    )
    replaced_data["Gender"] = replaced_data["Gender"].replace({"Male": 1, "Female": 0})
    replaced_data["Dominant_Hand"] = replaced_data["Dominant_Hand"].replace(
        {"Right": 1, "Left": 0}
    )
    replaced_data["Family_History"] = replaced_data["Family_History"].replace(
        {"Yes": 1, "No": 0}
    )
    replaced_data["Medication_History"] = replaced_data["Medication_History"].replace(
        {"Yes": 1, "No": 0}
    )
    replaced_data["Dementia_Gene"] = replaced_data["Dementia_Gene"].replace(
        {"Negative": 0, "Positive": 1}
    )
    replaced_data["Sleep_Quality"] = replaced_data["Sleep_Quality"].replace(
        {"Good": 1, "Poor": 0}
    )
    replaced_data["Smoking_Status"] = replaced_data["Smoking_Status"].replace(
        {"Current Smoker": 1, "Former Smoker": 1, "Never Smoked": 0}
    )
    replaced_data = replaced_data.drop(columns=["Prescription", "Dosage in mg"])
    replaced_data = replaced_data.rename(columns={"Smoking_Status": "Smoked"})

    replaced_data.fillna("N/A", inplace=True)
    return replaced_data


def combine_column_data(data: pd.DataFrame) -> pd.DataFrame:
    res = data.copy()
    res["Cumulative_Primary"] = (res["Education_Level"] != "No School").astype(int)

    res["Cumulative_Secondary"] = (
        (res["Education_Level"] != "No School")
        & (res["Education_Level"] != "Primary School")
    ).astype(int)

    res["Cumulative_Degree"] = (
        (res["Education_Level"] != "No School")
        & (res["Education_Level"] != "Primary School")
        & (res["Education_Level"] != "Secondary School")
    ).astype(int)
    res = res.drop(columns=["Education_Level"])

    return res


def main():
    print("New dataframe created, reformatting existing rows")
    replaced_df = replace_column_values("data/dementia_patients_health_data.csv")
    print(f"{count_dementia(replaced_df,0)} patients do not have dementia")
    print(f"{count_dementia(replaced_df,1)} patients have dementia")
    if replaced_df is not None:
        combined_data = combine_column_data(replaced_df)
        print("Data succesfully saved")
        if os.path.isfile("data/final_data.csv"):
            os.remove("data/final_data.csv")
            print("File deleted and created")
            # Note: The count_dementia function should be called after saving the new file
            # to ensure it reads the updated data.
        combined_data.to_csv("data/final_data.csv", index=False)
    else:
        print("Dataframe creation failed")


if __name__ == "__main__":
    main()
