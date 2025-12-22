from prompt import run_test
import json
from speech_ai import accept_speech
from flask import Flask, request, jsonify
import os

# Force TensorFlow to use CPU only - must be set BEFORE importing keras/tensorflow
os.environ['CUDA_VISIBLE_DEVICES'] = '-1'
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'  # Reduce TensorFlow logging

from tensorflow import keras
import joblib
from sklearn.preprocessing import LabelEncoder
from aggregation import aggregate_new_patient_data
from model_creation import create_model_and_train

app = Flask(__name__)

# pre load model so it doesn't have to load on each request
try:
    MODEL_PATH = "model_store/lifestylemodel.keras"
    SCALER_PATH = "model_store/minmax_scaler.pkl"
    FEATURES_PATH = "model_store/model_features.pkl"

    GLOBAL_MODEL = keras.models.load_model(MODEL_PATH)
    GLOBAL_SCALER = joblib.load(SCALER_PATH)
    GLOBAL_FEATURES = joblib.load(FEATURES_PATH)

    GLOBAL_LE = LabelEncoder()
    GLOBAL_LE.fit(["No Dementia", "Dementia"])
    
    print("Successfully loaded all ML artifacts.")

except FileNotFoundError as e:
    print(f"CRITICAL ERROR: Failed to load ML artifacts. Check your paths: {e}")
    raise SystemExit(f"Cannot start application without required model files: {e}")
except Exception as e:
    print(f"AN UNEXPECTED ERROR OCCURRED during model loading: {e}")
    raise SystemExit(f"Cannot start application due to model loading error: {e}")



@app.route("/articles", methods=["GET"])
def retrieveArticle():
    query = request.args.get("query", "").lower()
    file_path = None
    data = None

    match query:
        case "patient":
            file_path = "./data/article_1.json"
        case "doctor":
            file_path = "./data/article_2.json"
        case _:
            return jsonify(
                {
                    "status": "error",
                    "message": "Invalid article query. Try 'patient' or 'doctor'.",
                }
            ), 400

    try:
        with open(file_path, "r") as file:
            data = json.load(file)

        return jsonify(data), 200

    except FileNotFoundError:
        return jsonify(
            {"status": "error", "message": f"Article file not found at {file_path}"}
        ), 500

    except json.JSONDecodeError:
        return jsonify(
            {
                "status": "error",
                "message": f"Error decoding JSON from file at {file_path}. Check file format.",
            }
        ), 500


@app.route("/lifestyle", methods=["POST"])
def handle_post_lifestyle():
    """
    Takes in a post request made up of the following json
    {
    "answers":[
    {... question answers ...}
    ]
    }
    """
    if request.method == "POST":
        data = request.get_json()

        answers = data.get("answers")

        if not answers:
            return jsonify(
                {
                    "status": "error",
                    "message": "Missing 'answers' list in request body.",
                }
            ), 400

        try:
            classification_result = run_test(
                answers, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE
            )
        except Exception as e:
            return jsonify(
                {
                    "status": "error",
                    "message": f"Processing error during classification: {str(e)}",
                }
            ), 500

        if classification_result in ["0", "1"]:
            return jsonify(
                {"status": "success", "classification": classification_result}
            ), 200
        else:
            return jsonify(
                {
                    "status": "error",
                    "message": f"Classification failed: {classification_result}",
                }
            ), 500


@app.route("/classify", methods=["POST"])
def handle_post_speech():
    """
    Takes in a post request made up of the following json
    {
    "speech":"{speech_data}"
    }
    """
    if request.method == "POST":
        data = request.get_json()

        speech = data.get("speech")

        if not speech:
            return jsonify(
                {"status": "error", "message": "Missing speech in request body."}
            ), 400
        classification_result = accept_speech(speech)
        if classification_result in ["0", "1"]:
            return jsonify(
                {
                    "status": "success",
                    "transcript": speech,
                    "classification": classification_result,
                }
            ), 200
        else:
            return jsonify(
                {
                    "status": "error",
                    "message": f"Classification failed: {classification_result}",
                }
            ), 500


@app.route("/retrain", methods=["POST"])
def handle_retrain():
    """
    Takes in a POST request with a JSON array of patient data, aggregates it
    to training dataset and retrains the lifestyle modell.

    Expected JSON format:
    {
        "data": [
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
                "Diabetic": 0,
                "AlcoholLevel": 0.084973629,
                "HeartRate": 91,
                "BloodOxygenLevel": 96.23,
                "BodyTemperature": 36.22,
                "Weight": 57.6,
                "MRI_Delay": 33.42,
                "Age": 60,
                "Dominant_Hand": 0,
                "Gender": 0,
                "Family_History": 0,
                "Smoked": 0,
                "Dementia_Gene": 1,
                "Physical_Activity": "Sedentary",
                "Depression_Status": 0,
                "Cognitive_Test_Scores": 10,
                "Medication_History": 0,
                "Nutrition_Diet": "Low-Carb Diet",
                "Sleep_Quality": 1,
                "Chronic_Health_Conditions": "Diabetes",
                "Dementia": 0,
                "Cumulative_Primary": 1,
                "Cumulative_Secondary": 0,
                "Cumulative_Degree": 0
            }
        ]
    }
    """
    if request.method == "POST":
        request_data = request.get_json()

        patient_data = request_data.get("data")

        if not patient_data:
            return jsonify(
                {
                    "status": "error",
                    "message": "Missing 'data' array in request body.",
                }
            ), 400

        try:
            json_string = json.dumps(patient_data)

            aggregate_new_patient_data(
                json_data=json_string, file_to_add_to="data/final_data.csv", save=True
            )

            model, _, _ = create_model_and_train("data/final_data.csv")

            model.save("model_store/lifestylemodel.keras")

            global GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES
            GLOBAL_MODEL = keras.models.load_model("model_store/lifestylemodel.keras")
            GLOBAL_SCALER = joblib.load("model_store/minmax_scaler.pkl")
            GLOBAL_FEATURES = joblib.load("model_store/model_features.pkl")

            return jsonify(
                {
                    "status": "success",
                    "message": f"Model retrained successfully with {len(patient_data)} new record(s).",
                }
            ), 200

        except KeyError as e:
            return jsonify(
                {
                    "status": "error",
                    "message": f"Missing required field in patient data: {str(e)}",
                }
            ), 400
        except Exception as e:
            return jsonify(
                {
                    "status": "error",
                    "message": f"Error during retraining: {str(e)}",
                }
            ), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
