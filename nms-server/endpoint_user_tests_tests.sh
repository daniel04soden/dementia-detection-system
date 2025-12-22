URL="http://localhost:8080/api"

set -e

if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

curl -X POST "https://magestle.dev/api/mobile/stageone/insert" \
  -H "Content-Type: application/json" \
  -d '{
    "patientID": 3,
    "clockID": 2,
    "dateQuestion": "03/03/2023",
    "news": "nothin",
    "recallName": "Daniel",
    "recallSurname": "Knicker Pants",
    "recallNumber": "39",
    "recallStreet": "Sure Field Lane",
    "recallCity": "Paris"
  }'

echo "Test 1 Insert Passed"

curl -X POST "$URL/mobile/stagetwo/insert" \
  -H "Content-Type: application/json" \
  -d '{
    "patientID": 3,
    "memoryScore": 2,
    "recallRes": 3,
    "speakingScore": 2,
    "financialScore": 4,
    "medicineScore": 2,
    "transportScore": 1
  }'


echo "Test 2 Insert Passed"

curl -X POST "$URL/lifestyle/insert" \
  -H "Content-Type: application/json" \
  -d '{
    "patientID": 1,
    "grading": true,
    "diabetic": 1,
    "alcoholLevel": 6.9,
    "heartRate": 0,
    "bloodOxygen": 6.7,
    "bodyTemperature": 2.1, 
    "weight": 55.55, 
    "mriDelay": 2.2, 
    "age": 10, 
    "dominantHand": 1, 
    "gender": 0, 
    "familyHistory": 12, 
    "smoked": 2,
    "apoe4": 3,
    "physicalActivity": "Sedentary",
    "depressionStatus": 1,
    "cognitiveTestScores": 1,
    "medicationHistory": 0,
    "nutritionDiet": "Low-Carb Diet",
    "sleepQuality": 3,
    "chronicHealthConditions": "Diabetes",
    "education", "Primary"
  }'

