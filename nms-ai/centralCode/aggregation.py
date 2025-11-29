import pandas as pd

def aggregate_new_patient_data(df:pd.Dataframe,file_to_add_to:str):
    original_df = pd.read_csv(file_to_add_to)
    
    return pd.concat([original_df,df])

def main():
    pass


if __name__ == "__main__":
    main()
