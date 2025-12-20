curl -i -X POST \
  -H "Accept: application/json" \
  -F "file=@/Users/danielsoden/Developer/testenv/pythontest/harvard.wav" \
  http://127.0.0.1:5000/speech
