# NMS-AI Documentation
- By Daniel Soden

- The NMS-AI directory code is made to detect dementia through speech analysis and lifestyle factors using machine learning while also providing up-to-date research articles on dementia through a research crew.

- This documentation was created to guide users and other developers on the team through the structure and functionality of the codebase.
- It covers the main components, including the research crew model, Keras model for lifestyle analysis, and speech detection usage.

## Table of Contents

- [Overview](#overview)
- [Dementia Research Crew](#dementia-research-crew)
- [Keras Model](#keras-model)
- [Speech Processing](#speech-processing)
- [Lifestyle Detection](#lifestyle-detection)
- [Setup & Running](#setup--running)
    -   [Environment](#environment)
    -   [Dockerfile](#dockerfile)
    -   [Running Dockerfile](#running-dockerfile)
    -   [Data](#data)
    -   [Commands](#commands)

# Overview
- Brief intro to dementia detection system:

- Two key directories: centralCode and dementia_research_crew
    - centralCode: Includes a Keras model for lifestyle-based dementia prediction, speech analysis via a pre-trained openai model on speech data.  
    - dementia_research_crew: Research Crew used to aggregate articles on dementia research for patients and doctors 
- Quick start prerequisites and env setup
    - Python 3.12+
    - docker/docker-compose 
    - Required libraries (see requirements.txt files)
    - environment variables for ollama, openai and deepseek API keys 
# Dementia Research Crew
- Purpose
- Key Files
  - dementia_research_crew/src/dementia_research_crew/config/agents.yaml
    ```yml
    dementia_news_analyst:
    role: >
    Dementia News Analyst
    goal: >
    Create two simple reports based on dementia news sources and research findings both for patients and normal civillians to use, as well as for meidcal professionals and researchers to use
    backstory: >
    You're a meticulous reporter with a considerate approach to reporting on news. You're known for
    your ability to turn complex data and hard news stories into easy to digest and consise summaries while retaining the facts, making
    it easy for both patients and meidcal professionals to understand the information you are putting out there.

    dementia_news_reporter:
    role: >
    Dementia News Reporter 
    goal: >
    Create two detailed reports based on dementia news sources and research findings for doctors and other medical professionals to use
    backstory: >
    You're a meticulous reporter with a keen eye for detail. You're known for
    your ability to turn complex data and hard news stories into clear and concise posts, making
    it easy for civiallians and medical professionals to understand and act on the information you provide.
    ```
    - The yml file above defines two agents: dementia_news_analyst and dementia_news_reporter, 
        each with specific roles, goals, and backstories to guide their behavior in generating reports on dementia research.

- dementia_research_crew/src/dementia_research_crew/config/tasks.yaml
    ```yml
    dementia_news_research_task:
      description: >
        Research the latest dementia news and developments that would be relevant to both patients and professionals alike
        Focus on:
        - Treatment breakthroughs and new therapies
        - Lifestyle tips and prevention strategies  
        - Support resources and community programs
        - Patient success stories and hope-inspiring developments
      expected_output: >
        Two different lists, one with ten articles summarised and prepared for any consumer with very little knowledge to read. And another one which contains a detailed insight into the latest developments for medical professionals in dementia developments.
      agent: dementia_news_analyst


    dementia_news_report_task:
      description: >
        Reformat the two lists of these articles, into json associative arrays
        Focus on:
        - Ensuring the articles do not lead to dead links 
        - Have consise but descriptive snippets 
      expected_output: >
        Two json files with ten values storing a headline,a snippet summary of the article that is relevant to professionals, and the url of said article 
      agent: dementia_news_reporter
    ```
    - The tasks defined above give the individual agents their goals and from here we are able to ensure that not only are the articles relevant but are also formatted
    correctly for the users reading the snippets and headers of these articles when they appear in the web and app portal


- dementia_research_crew/src/dementia_research_crew/main.py

- Key entrypoint function:

```python
# dementia_research_crew/src/dementia_research_crew/main.py:29
def run():
    inputs = {
        'topic': 'Dementia detection research',
        'current_year': str(datetime.now().year)
    }

    curr_crew = DementiaResearchCrew().crew()
    for agents in curr_crew.agents:
        agents.llm = my_ai

    curr_crew.kickoff(inputs=inputs)
```
 - The code above shows the most critical part of my own work in the crew.py which shows the crew kicking off its research and given it some context
 
- dementia_research_crew/out.py

  Conversion function:

```python
# dementia_research_crew/out.py:4
def read_ai_articles(file_name: str):
    md_json = open(file_name, "r")
    markdown_string = md_json.read()
    md_json.close()
    my_snippets = pyparseit.parse_markdown_string(markdown_string, language='json')
    for i, snippet in enumerate(my_snippets):
        json_data = json.loads(snippet.content)
        json_str = json.dumps(json_data, indent=4)
        with open(f"../centralCode/data/article_{i+1}.json", "w") as new_json:
            new_json.write(json_str)
```
- THis function parses markdown containing JSON code blocks from the crew output and writes them to centralCode/data/article_1.json and article_2.json which will then be called within the  cron job periodically and retrieved in the golang nms-server backend
 
- How To Run:
```bash
cd dementia_research_crew/src/dementia_research_crew
curl -LsSf https://astral.sh/uv/install.sh | sh # Installing uv as a dependency to run crewai
uv sync
source .venv/bin/activate
crewai run 
.venv/bin/python out.py 
```
 
- By running this script, the research crew will generate two json files: article_1.json and article_2.json in the dementia_research_crew/src/dementia_research_crew/ directory.
- The out.py script is used to format and save the output from the crew run into these json files for later use by the nms-server golang webserver.

# Keras Model
 
- Purpose
- The keras model is created as a means of predicting the likelihood of dementia based on lifestyle factors. 
- It leverages metrics present in the dementia patient lifestyle dataset which reflect patterns found in people with dementia in order to train the model using techniques such as one hot encoding, class weighting, and early stopping.
- Below is an example input json string for the model used in our flask web API:
 
```json
'{
"answers":[{
"Diabetic":0,
"AlcoholLevel":0.05,
"HeartRate":80,
"BloodOxygen":97.5,
"BodyTemperature":36.8,
"Weight":70.0,
"MRI_Delay":40.0,
"Age":65,
"Dominant_Hand":0,
"Gender":0,
"Family_History":0,
"Smoked":1,
"Dementia_gene":0,
"Physical_Activity":"Moderate Activity",
"Depression_Status":0,
"Cognitive_Test_Scores":9,
"Medication_History":0,
"Nutrition_Diet":"Mediterranean Diet",
"Sleep_Quality":0,
"Chronic_Health_Conditions":"N/A",
"Cumulative_Primary":"FALSE",
"Cumulative_Secondary":"FALSE",
"Cumulative_Degree":"FALSE"
}]
}'
```

- This json will be passed once a user completes a lifestyle questionnaire on the 
- Key pre-processing functions:

```python
# centralCode/data_format.py

def replace_column_values(file_name: str) -> pd.DataFrame:
    replaced_data = pd.read_csv(file_name)
    replaced_data["Weight"] = replaced_data["Weight"].round(1)
    replaced_data["Depression_Status"] = replaced_data["Depression_Status"].replace({"Yes": 1, "No": 0})
    replaced_data["Gender"] = replaced_data["Gender"].replace({"Male": 1, "Female": 0})
    replaced_data["Dominant_Hand"] = replaced_data["Dominant_Hand"].replace({"Right": 1, "Left": 0})
    replaced_data["Family_History"] = replaced_data["Family_History"].replace({"Yes": 1, "No": 0})
    replaced_data["Medication_History"] = replaced_data["Medication_History"].replace({"Yes": 1, "No": 0})
    replaced_data["Dementia_Gene"] = replaced_data["Dementia_Gene"].replace({"Negative": 0, "Positive": 1})
    replaced_data["Sleep_Quality"] = replaced_data["Sleep_Quality"].replace({"Good": 1, "Poor": 0})
    replaced_data["Smoking_Status"] = replaced_data["Smoking_Status"].replace({"Current Smoker": 1, "Former Smoker": 1, "Never Smoked": 0})
    replaced_data = replaced_data.drop(columns=["Prescription", "Dosage in mg"])
    replaced_data = replaced_data.rename(columns={"Smoking_Status": "Smoked"})
    replaced_data.fillna("N/A", inplace=True)
    return replaced_data


def combine_column_data(data: pd.DataFrame) -> pd.DataFrame:
    res = data.copy()
    res["Cumulative_Primary"] = (res["Education_Level"] != "No School").astype(int)
    res["Cumulative_Secondary"] = ((res["Education_Level"] != "No School") & (res["Education_Level"] != "Primary School")).astype(int)
    res["Cumulative_Degree"] = ((res["Education_Level"] != "No School") & (res["Education_Level"] != "Primary School") & (res["Education_Level"] != "Secondary School")).astype(int)
    res = res.drop(columns=["Education_Level"])
    return res


def count_dementia(df: pd.DataFrame, has: int) -> int:
    demented_patients = df[df["Dementia"] == has]
    return len(demented_patients)
```

- The code above adjusts the lifestyle data in order to fit more in line with what a binary classification model would be capable of understanding and predicting from
- Aggregation function used for retraining:
```python
# centralCode/aggregation.py:5

def aggregate_new_patient_data(json_data: str, file_to_add_to: str, save: bool) -> pd.DataFrame:
    data = json.loads(json_data)
    new_df = pd.DataFrame(data)
    original_df = pd.read_csv(file_to_add_to)
    new_df = new_df[original_df.columns]
    combined_df = pd.concat([original_df, new_df], ignore_index=True)
    if save:
        combined_df.to_csv(file_to_add_to, index=False)
    return combined_df
```

- This function is a core feature of the 
# Speech Processing 
- Using the ADReSS Challenge 2020 dataset, my speech detection file analyses strings taken from .wav recordings parsed through assembly ai to identify patterns indicative of dementia via symbols like &s uhms and pauses through dashes and other indactors. The openai model is trained on a  variety of excerpts from the dataset to recognize these speech patterns that are denoted with 0s and 1s indicating the presence or absence of dementia.

- Example input for the speech detection model:

- Key function used by the API for speech classification:

```python
# centralCode/speech_ai.py:18
def accept_speech(par: str) -> str:
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
```
- This sends a transcript to the fine-tuned Open-AI model and returns a raw label, expected to be "0" or "1". 

- This will obviously only work with my required API key and model name that is trained on the speech data

# Lifestyle detection:

```python
# centralCode/prompt.py:195

def run_test(answers: list, loaded_model, scaler, model_features, global_le) -> str:
    X_processed = pd.get_dummies(pd.DataFrame(answers), columns=pd.DataFrame(answers).select_dtypes(include=["object", "bool"]).columns, drop_first=True)
    X_aligned = X_processed.reindex(columns=model_features, fill_value=0)
    X_scaled = scaler.transform(X_aligned)
    y_pred_probs = loaded_model.predict(X_scaled, verbose=0)
    y_pred_binary = (y_pred_probs > 0.3).astype(int)
    return str(y_pred_binary.flatten()[0])
```

- CLI sample and assets loader are available in centralCode/prompt.py if you need example payloads.
- Returns a 0 or 1 indicating dementia likelihood.
# Setup & Running
## Environment
 
- Using docker compose files, set up the environment by running - this should activate the web api and be able to accept requests at port 5000

## Dockerfile
```Dockerfile
FROM ubuntu:24.04

RUN apt-get update && apt-get install -y \
	python3 \
	python3-pip \
	python3-venv \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /app

RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY *.py ./

COPY model_store/ ./model_store/
COPY ADReSS-IS2020-data-train/ ./ADReSS-IS2020-data-train/
COPY ADReSS-IS2020-data-test/ ./ADReSS-IS2020-data-test/
COPY data/ ./data/

EXPOSE 5000

CMD ["python", "web.py"]
```



- The Dockerfile above makes an Ubuntu container with all required python packages install and mounts the CSV data, models and other required data.
## Running Dockerfile

```yml
version: '3.8'

services:
  dementia-detection-api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: dementia-detection-api
    ports:
      - "5000:5000"
    environment:
      - GENERAL_KEY=${GENERAL_KEY:-default_key}
    volumes:
      - ./model_store:/app/model_store
      - ./ADReSS-IS2020-data-train:/app/ADReSS-IS2020-data-train:ro
      - ./ADReSS-IS2020-data-test:/app/ADReSS-IS2020-data-test:ro
      - ./data:/app/data
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:5000/"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

- The compose file ensures the environment variables are properly recognised and runs health checks on the flask app.
- Although the compose file might have been unnecessary as  there is only one container, it works a lot better for mounting the volumes and expose the network correctly so it can interact with other containers on the server.

 
- The research crew does not need to be activated in a sense, it is ran as a cron job on the server which generates the jsona articles once a week and 
these are called via the nms-server golang webserver.
 
```bash
 
docker system prune -a
docker-compose build
docker-compose up
 
``` 
 
## Data
 
- The following files are not present in the git repo as they are too large to be store there.
- Please contact the group leader (danielsoden04@gmail.com) to obtain these files if needed.
    - ADReSS_IS2020-data-test/
        - meta_data.txt
        - meta_data.csv
        - cc_meta_data.jsonl
        - cd_meta_data.jsonl
    - ADReSS_IS2020-data-train/
        - meta_data.csv
        - cc_meta_data.jsonl
        - cd_meta_data.jsonl
    - data/
        - final_data.csv
        - adjusted_dementia.csv
        - article_1.json
        - article_2.json
 
## Commands
 
- To run the web.py api individually without docker, use the following command:
- This will set up a virtual environment, install the required packages, run the web.py file and then run the test script located in centralCode/test.sh to verify functionality.
- The test script runs a series of curl commands to test the endpoints of the web API and can be used as a basis to create interaction for the the nms-server golang webserver.
 
```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python3 web.py
 
./centralCode/test.sh
```

### API Endpoints (centralCode/web.py)

```python
# GET /articles
@app.route("/articles", methods=["GET"])
def retrieveArticle():
    # query: "patient" | "doctor"
    # serves pre-generated JSON articles from centralCode/data

# POST /lifestyle
@app.route("/lifestyle", methods=["POST"])
def handle_post_lifestyle():
    # body: {"answers": [{...}]}
    # runs prompt.run_test with preloaded model artifacts

# POST /classify
@app.route("/classify", methods=["POST"])
def handle_post_speech():
    # body: {"speech": "..."}
    # calls speech_ai.accept_speech and returns classification

# POST /retrain
@app.route("/retrain", methods=["POST"])
def handle_retrain():
    # body: {"data": [{...}, {...}]}
    # aggregates new data, retrains model, refreshes artifacts
```

