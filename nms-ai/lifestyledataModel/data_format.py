import pandas as pd
import os

pd.set_option("display.max_rows", None)


def replace_column_values(file_name: str) -> pd.DataFrame:
    replaced_data = pd.read_csv(file_name)
    # Simplfying data
    replaced_data["Weight"] = replaced_data["Weight"].round(1)
    # All "boolean"-like data

    # replaced_data[7].replace({"Positive": 1, "Negative": 0}, inplace=True)
    replaced_data["Depression_Status"].replace({"Yes": 1, "No": 0}, inplace=True)
    replaced_data["Gender"].replace({"Male": 1, "Female": 0}, inplace=True)
    replaced_data["Dominant_Hand"].replace({"Right": 1, "Left": 0}, inplace=True)
    replaced_data["Family_History"].replace({"Yes": 1, "No": 0}, inplace=True)
    replaced_data["Medication_History"].replace({"Yes": 1, "No": 0}, inplace=True)
    replaced_data["Sleep_Quality"].replace({"Good": 1, "Poor": 0}, inplace=True)
    replaced_data["Smoking_Status"].replace(
        {"Current Smoker": 1, "Former Smoker": 1, "Never Smoked": 0}, inplace=True
    )
    replaced_data.rename(columns={"Smoking_Status": "Smoked"}, inplace=True)

    replaced_data.fillna("N/A", inplace=True)
    return replaced_data


def combine_column_data(data: pd.DataFrame) -> pd.DataFrame:
    res = data.copy()
    res["Cumulative_Primary"] = res["Education_Level"] != "No School"

    res["Cumulative_Secondary"] = (res["Education_Level"] != "No School") & (
        res["Education_Level"] != "Primary School"
    )

    res["Cumulative_Degree"] = (
        (res["Education_Level"] != "No School")
        & (res["Education_Level"] != "Primary School")
        & (res["Education_Level"] != "Secondary School")
    )
    res.drop(columns=["Education_Level"], inplace=True)
    res["Cumulative_Primary"].replace({"TRUE": 1, "FALSE": 0}, inplace=True)
    res["Cumulative_Secondary"].replace({"TRUE": 1, "FALSE": 0}, inplace=True)
    res["Cumulative_Degree"].replace({"TRUE": 1, "FALSE": 0}, inplace=True)
    return res


def main():
    print("New dataframe created, reformatting existing rows")
    replaced_df = replace_column_values("data/dementia_patients_health_data.csv")
    if replaced_df is not None:
        combined_data = combine_column_data(replaced_df)
        print("Data succesfully saved")
        if os.path.isfile("data/final_data.csv"):
            os.remove("data/final_data.csv")
            print("File deleted and created")
        combined_data.to_csv("data/final_data.csv")
    else:
        print("Dataframe creation failed")


if __name__ == "__main__":
    main()
