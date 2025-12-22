# Use this to quickly test the different endpoints of the dementia system

# Change the URL here
URL="http://localhost:8080/api"

set -e

if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

curl -X POST "$URL/admin/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.ie",
    "password": "'"$TEST_PASSWORD"'",
    "firstName": "Gary",
    "lastName": "Whirlpools",
    "phone": "0125439453"
  }'

echo "Admin Creation Test Passed"

curl -X POST "$URL/admin/clinics" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dublin Hospital",
    "phone": "0452348854",
    "county": "Dublin",
    "eircode": "P32D291"
  }'

echo "Clinic Creation Test Passed"

curl -X POST "$URL/web/signup" \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "doctor1@test.ie",
    "doctorNO": "98538424",
    "password": "'"$TEST_PASSWORD"'",
    "firstName": "Mary",
    "lastName": "Jane",
    "phone": "09764323457",
    "clinicID": 1
  }'

echo "Doctor Creation Test Passed"

curl -X POST "$URL/admin/approve?id=2" \
  -H 'Content-Type: application/json'

echo "Doctor Approve Test Passed"

curl -X POST "$URL/mobile/signup" \
  -H "content-type: application/json" \
  -d '{
    "email": "patient1@test.ie",
    "password": "'"$TEST_PASSWORD"'",
    "firstname": "Steve",
    "lastname": "Jobs",
    "phone": "09764323457",
    "eircode": "P31D251",
    "clinicid": 1
  }'

echo "Patient Creation Test Passed"


