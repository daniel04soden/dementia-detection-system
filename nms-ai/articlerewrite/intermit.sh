#!/usr/bin/env bash

source ./bin/activate
./bin/python3 main.py

echo "Done refresh of json for ai news articles"

cat json_articles.json

