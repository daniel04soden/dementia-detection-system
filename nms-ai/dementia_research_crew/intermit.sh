#!/usr/bin/env bash

source ./.venv/bin/activate
crewai run
.venv/bin/python3 out.py

echo "Done refresh of json for ai news articles"
