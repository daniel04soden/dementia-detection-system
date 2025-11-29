import openai
from dotenv import load_dotenv
import os
import pandas as pd
from random import randint

MODEL_NAME = 'ft:gpt-4.1-nano-2025-04-14:personal:detection-experiment:CbNqHnUf'
load_dotenv()
API_KEY = os.getenv('GENERAL_KEY')
SYSTEM_PROMPT = """
You are a intelligent assistant designed to identify through speech someones likelihood of having dementia
You are an intelligent clinical assistant tasked with classifying spoken transcripts
from a subject as either '0' (Non-Dementia) or '1' (Dementia).
Analyse the linguistic features and content of the transcript to determine the classification.
"""

def accept_speech(par:str)->str:
    client = openai.OpenAI(api_key=API_KEY) 
    response = client.chat.completions.create(
        model=MODEL_NAME,
        messages=[
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": par}
        ],
        temperature=0.0,
        max_tokens=50, 
    )
    raw_reply = response.choices[0].message.content.strip()
    return raw_reply

def get_random_data(file_name:str,demented:int)->str:
    df = pd.read_csv(file_name)
    dementia_df = df[df["dementia"] == demented]
    demented_speech = dementia_df["transcript"]
    n = len(demented_speech)
    random_val = randint(0,n-1)
    para = demented_speech.iloc[random_val]

    print(para)
    return para

        



def main():
    file_name = "ADReSS-IS2020-data-train/train/dementia_data.csv"
    para_demented = get_random_data(file_name,1)
    para_non_demented = get_random_data(file_name,0)
    demented_response = accept_speech(para_demented)
    non_demeted_response = accept_speech(para_non_demented)
    print(demented_response)
    print(non_demeted_response)

if __name__ == "__main__":
    main()
