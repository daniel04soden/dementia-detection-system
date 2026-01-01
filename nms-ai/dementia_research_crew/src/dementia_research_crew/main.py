#!/usr/bin/env python
import sys
import warnings
from datetime import datetime
from dementia_research_crew.crew import DementiaResearchCrew
import os
from crewai import LLM
from dotenv import load_dotenv

load_dotenv()

# Access the environment variable
# deep_api_key = os.getenv("DEEPSEEK_API_KEY")
# ollama_base_url = "http://localhost:11434"
deepseek_base_url = "https://api.deepseek.com/v1"

# Defining LLM 
my_ai = LLM(
    model="deepseek-reasoner",
    # model="ollama/llama3:8b",
    #base_url=ollama_base_url,
    base_url=deepseek_base_url,
    api_key=os.getenv("DEEPSEEK_API_KEY"),
     # temperature=0.6
 )

warnings.filterwarnings("ignore", category=SyntaxWarning, module="pysbd")

def run():
    """
    Run the crew.
    """
    inputs = {
        'topic': 'Dementia detection for doctors and patients',
        'current_year': str(datetime.now().year)
    }

    try:
        curr_crew = DementiaResearchCrew().crew()
        for agents in curr_crew.agents:
            agents.llm = my_ai

        curr_crew.kickoff(inputs=inputs)
    except Exception as e:
        raise Exception(f"An error occurred while running the crew: {e}")


def train():
    """
    Train the crew for a given number of iterations.
    """
    inputs = {
        "topic": "AI LLMs",
        'current_year': str(datetime.now().year)
    }
    try:
        DementiaResearchCrew().crew().train(n_iterations=int(sys.argv[1]), filename=sys.argv[2], inputs=inputs)

    except Exception as e:
        raise Exception(f"An error occurred while training the crew: {e}")

def replay():
    """
    Replay the crew execution from a specific task.
    """
    try:
        DementiaResearchCrew().crew().replay(task_id=sys.argv[1])

    except Exception as e:
        raise Exception(f"An error occurred while replaying the crew: {e}")

def test():
    """
    Test the crew execution and returns the results.
    """
    inputs = {
        "topic": "AI LLMs",
        "current_year": str(datetime.now().year)
    }
    
    try:
        DementiaResearchCrew().crew().test(n_iterations=int(sys.argv[1]), eval_llm=sys.argv[2], inputs=inputs)

    except Exception as e:
        raise Exception(f"An error occurred while testing the crew: {e}")
