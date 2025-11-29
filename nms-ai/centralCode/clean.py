import csv
import json
import os
import re as regex  # Imported as 'regex' as its my first exposure to it and want to understand it more so remind myself

import pandas as pd


def get_transcript(dir: str, id: str, dir_contents: list[str]):
    for file_name in dir_contents:
        if file_name.startswith(id):
            with open(f"{dir}/{file_name}",encoding='utf8') as my_file:
                content = my_file.read()
                return content
    return None



def clean_transcript(text):
    if text is None:
        return ""

    final_transcript_parts = []
    lines = text.strip().split("\n")
    find_time_stamp = r" \d+_\d+[\s\n]*"

    for line in lines:
        if line.startswith("*PAR:"):
            clean_line = line[len("*PAR:"):].strip()
            clean_line_minus_nums = regex.sub(find_time_stamp, "", clean_line)

            if clean_line_minus_nums:
                final_transcript_parts.append(clean_line_minus_nums)
        elif line.startswith(("@", "%")):
            continue

    return " ".join(final_transcript_parts)


def change_to_csv_format(dir: str, file_name: str, c_or_d: str):
    res = []
    if c_or_d == "":
        transcript_dir = f"{dir}/transcription"
    else:
        transcript_dir = f"{dir}/transcription/{c_or_d}"
    transcript_dir_contents = os.listdir(transcript_dir)

    with open(f"{dir}/{file_name}") as my_file:
        lines = my_file.readlines()

    header_line = [
        x.strip() for x in regex.split(r"\s*;\s*", lines[0].strip())
    ]  # Fixes ; split in cd/cc_data
    header_line.append("transcript")
    res.append(header_line)

    for i in range(1, len(lines)):
        lines[i] = lines[i].strip()
        linee: list[str] = [x.strip() for x in regex.split(r"\s*;\s*", lines[i])]
        # Fixes ; split in cd/cc_data

        transcript = clean_transcript(
            get_transcript(transcript_dir, linee[0], transcript_dir_contents)
        )

        if transcript is None:
            transcript = ""

        linee.append(transcript)
        res.append(linee)

    file_name_prefix = file_name.replace(".txt", "")
    output_csv_path = os.path.join(dir, f"{file_name_prefix}.csv")

    with open(output_csv_path, "w", newline="", encoding="utf-8") as csvfile:
        csv_writer = csv.writer(csvfile, delimiter=",")
        csv_writer.writerows(res)


def combine_csvs(dir: str, file_one: str, file_two: str):
    """
    Going in with the understanding that file one is non-dementia
    and file two is dementia
    """
    new_name = "dementia_data.csv"

    files = [f"{dir}/{file_one}", f"{dir}/{file_two}"]

    df_one = pd.read_csv(files[0], delimiter=",",encoding='utf8')

    df_one["dementia"] = 0

    df_two = pd.read_csv(files[1], delimiter=",",encoding='utf8')

    df_two["dementia"] = 1

    res = [df_one, df_two]
    combined_df = pd.concat(res, ignore_index=True)

    combined_df.to_csv(f"{dir}/{new_name}", index=False)


def convert_to_jsonl(dir: str, file_name: str):
    file_name_prefix = file_name.replace(
        ".txt", ""
    )  # TODO - reeval whether needed here...
    file_name_csv = f"{file_name_prefix}.csv"

    df = pd.read_csv(os.path.join(dir, file_name_csv), sep=";", header=0)
    df.columns = df.columns.str.strip()

    output_jsonl_path = os.path.join(dir, f"{file_name_prefix}.jsonl")

    df.to_json(output_jsonl_path, orient="records", lines=True)


def combine_jsonls(dir: str, file_one: str, file_two: str):
    """
    Going in with the understanding that file one is non-dementia
    and file two is dementia
    """
    res_file_name = "dementia_data.jsonl"
    with open(f"{dir}/{res_file_name}", "w") as output_file:
        with open(f"{dir}/{file_one}", "r") as first_file:
            for line in first_file:
                data = json.loads(line.strip())
                data["dementia"] = 0
                output_file.write(json.dumps(data) + "\n")

        with open(f"{dir}/{file_two}", "r") as second_file:
            for line in second_file:
                data = json.loads(line.strip())
                data["dementia"] = 1
                output_file.write(json.dumps(data) + "\n")
    print("Json files combined")


def reformat_csv_openai_jsonl(dir: str, file_name):
    """
    This function will take the jsonl file and format it in the following format:

        "message":[
        {"role":"system","content":system},
        {"role":"user","content":""},
        {"role":"assistant","content":""}
        ]
    """

    df = pd.read_csv(f"{dir}/{file_name}",encoding='utf8')
    system = """

    You are a intelligent assistant designed to identify through speech someones likelihood of having dementia
    You are an intelligent clinical assistant tasked with classifying spoken transcripts
        from a subject as either '0' (Non-Dementia) or '1' (Dementia).
        Analyse the linguistic features and content of the transcript to determine the classification.

    """
    with open(f"{dir}/final.jsonl", "w") as f:
        for _, row in df.iterrows():
            label = str(row["dementia"])
            transcript = row["transcript"]

            openai_format = {
                "messages": [
                    {"role": "system", "content": system},
                    {"role": "user", "content": transcript},
                    {"role": "assistant", "content": label},
                ]
            }
            json.dump(openai_format, f)
            f.write("\n")


def train_data_fix():
    base_dir = "ADReSS-IS2020-data-train/train"

    change_to_csv_format(base_dir, "cc_meta_data.txt", "cc")
    print("\n Break \n")
    change_to_csv_format(base_dir, "cd_meta_data.txt", "cd")
    combine_csvs(base_dir, "cc_meta_data.csv", "cd_meta_data.csv")
    reformat_csv_openai_jsonl(base_dir, "dementia_data.csv")

    convert_to_jsonl(base_dir, "cc_meta_data.txt")
    convert_to_jsonl(base_dir, "cd_meta_data.txt")

    combine_jsonls(base_dir, "cc_meta_data.jsonl", "cd_meta_data.jsonl")


def adjust_test_data(base_dir: str, file_name: str):
    df = pd.read_csv(f"{base_dir}/{file_name}",encoding='utf8')

    df["gender"] = df["gender"].replace({1: "female", 0: "male"})

    df.to_csv(f"{base_dir}/{file_name}", index=False)


def test_data_fix():
    base_dir = "ADReSS-IS2020-data-test/test"
    # No need for csv conversion
    change_to_csv_format(base_dir, "meta_data.txt", "")
    adjust_test_data(base_dir, "meta_data.csv")

    reformat_csv_openai_jsonl(base_dir, "meta_data.csv")


def main():
    train_data_fix()
    test_data_fix()


if __name__ == "__main__":
    main()
