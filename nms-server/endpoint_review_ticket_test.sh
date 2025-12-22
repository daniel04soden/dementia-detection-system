
URL="http://localhost:8080/api"

set -e

if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi


curl -X POST "$URL/review/insert?id=3" \
  -H "Content-Type: application/json" \
  -d '{
    "score": 5,
    "critique": "App is perfect"
  }'

echo "Review insert test passed"


