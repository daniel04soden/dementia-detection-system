from prompt import run_test
import json
from speech_ai import accept_speech
from flask import Flask, request, jsonify
import keras
import joblib
from sklearn.preprocessing import LabelEncoder

app = Flask(__name__)

# pre load model so it doesn't have to load on each request
try:
    MODEL_PATH = 'model_store/lifestylemodel.keras'
    SCALER_PATH = 'model_store/minmax_scaler.pkl'
    FEATURES_PATH = 'model_store/model_features.pkl'

    GLOBAL_MODEL = keras.models.load_model(MODEL_PATH)
    GLOBAL_SCALER = joblib.load(SCALER_PATH)
    GLOBAL_FEATURES = joblib.load(FEATURES_PATH)
    
    GLOBAL_LE = LabelEncoder()
    GLOBAL_LE.fit(['No Dementia', 'Dementia']) 
    
except FileNotFoundError as e:
    print(f"CRITICAL ERROR: Failed to load ML artifacts. Check your paths: {e}")
except Exception as e:
    print(f"AN UNEXPECTED ERROR OCCURRED during model loading: {e}")



@app.route('/articles', methods=['GET'])
def retrieveArticle():
    query = request.args.get('query', '').lower()
    file_path = None
    data = None

    match query:
        case 'patient':
            file_path = './data/article_1.json'
        case 'doctor':
            file_path = './data/article_2.json'
        case _:
            return jsonify({
                "status": "error",
                "message": "Invalid article query. Try 'patient' or 'doctor'."
            }), 400

    try:
        with open(file_path, 'r') as file:
            data = json.load(file) 
            
        return jsonify(data), 200

    except FileNotFoundError:
        return jsonify({
            "status": "error",
            "message": f"Article file not found at {file_path}"
        }), 500
        
    except json.JSONDecodeError:
        return jsonify({
            "status": "error",
            "message": f"Error decoding JSON from file at {file_path}. Check file format."
        }), 500

@app.route('/lifestyle', methods=['POST'])
def handle_post_lifestyle():
    """
    Takes in a post request made up of the following json
    {
    "answers":[
    {... question answers ...}
    ]
    }
    """
    if request.method == 'POST':
        data = request.get_json()
        
        answers = data.get('answers')

        if not answers:
            return jsonify({
                "status": "error",
                "message": "Missing 'answers' list in request body." 
            }), 400
        
        try:
            classification_result = run_test(answers, GLOBAL_MODEL, GLOBAL_SCALER, GLOBAL_FEATURES, GLOBAL_LE)
        except Exception as e:
            return jsonify({
                "status": "error",
                "message": f"Processing error during classification: {str(e)}"
            }), 500
            
        if classification_result in ['0', '1']:
            return jsonify({
                "status": "success",
                "classification": classification_result
            }), 200
        else:
            return jsonify({
                "status": "error",
                "message": f"Classification failed: {classification_result}"
            }), 500


@app.route('/classify', methods=['POST'])
def handle_post_speech():
    """
    Takes in a post request made up of the following json
    {
    "speech":"{speech_data}"
    }
    """
    if request.method == 'POST':
        data = request.get_json()
        
        speech = data.get('speech')

        if not speech:
            return jsonify({
                "status": "error",
                "message": "Missing speech in request body."
            }), 400
        classification_result = accept_speech(speech)
        if classification_result in ['0', '1']:
            return jsonify({
                "status": "success",
                "transcript": speech,
                "classification": classification_result
            }), 200
        else:
            return jsonify({
                "status": "error",
                "message": f"Classification failed: {classification_result}"
            }), 500



if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
