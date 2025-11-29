from crewai.tools import tool
from crewai.project import CrewBase, agent, crew, task
from datetime import datetime
from crewai import Agent, Crew, Process, Task
from crewai.agents.agent_builder.base_agent import BaseAgent
from typing import List
import warnings
from crewai.process import Process
from textwrap import dedent
from langchain_community.tools import DuckDuckGoSearchRun
from crewai import LLM
from dotenv import load_dotenv
import os

load_dotenv()
my_ai = LLM(
     model="deepseek/deepseek-chat",
     api_key=os.getenv("DEEPSEEK_API_KEY"),
     temperature=0.6
)
warnings.filterwarnings("ignore", category=SyntaxWarning, module="pysbd")

@tool('DuckDuckGoSearch')
def search(search_query: str):
    """
    function created to give agents acess to web
    """
    return DuckDuckGoSearchRun().run(search_query)


@CrewBase
class DementiaResearchCrew():
    """DementiaResearchCrew crew"""

    @agent
    def dementia_news_analyst(self) -> Agent:
        return Agent(
            role='Dementia News Analyst',
            goal='Create two simple reports based on dementia news sources and research findings both for patients and normal civillians to use, as well as for medical professionals and researchers to use',
            backstory=dedent("""\
                You're a meticulous reporter with a considerate approach to reporting on news. You're known for
                your ability to turn complex data and hard news stories into easy to digest and concise summaries while retaining the facts, making
                it easy for both patients and medical professionals to understand the information you are putting out there.
                """),
            verbose=True,
            tools=[search]
        )

    @agent
    def dementia_news_reporter(self) -> Agent:
        return Agent(
            role='Dementia News Reporter',
            goal='Create two detailed reports based on dementia news sources and research findings for doctors and other medical professionals to use',
            backstory=dedent("""\
                You're a meticulous reporter with a keen eye for detail. You're known for
                your ability to turn complex data and hard news stories into clear and concise posts, making
                it easy for civiallians and medical professionals to understand and act on the information you provide.
                """),
            verbose=True
        )

    @task
    def dementia_news_research_task(self, agent):
        return Task(
            description=dedent(f"""\
                Research the latest dementia news and developments that would be relevant to both patients and professionals alike
                Focus on:
                - Treatment breakthroughs and new therapies
                - Lifestyle tips and prevention strategies  
                - Support resources and community programs
                - Patient success stories and hope-inspiring developments
                """),
            expected_output=dedent("""\
                Two different lists, one with ten articles summarised and prepared for any consumer with very little knowledge to read. And another one which contains a detailed insight into the latest developments for medical professionals in dementia developments.
                """),
            async_execution=True,
            agent=agent
        )

    @task
    def dementia_news_report_task(self, agent):
        return Task(
            description=dedent(f"""\
                Reformat the two lists of these articles, into json associative arrays
                Focus on:
                - Ensuring the articles do not lead to dead links 
                - Have consise but descriptive snippets 
                """),
            expected_output=dedent("""\
                Two json files with ten values storing a headline,a snippet summary of the article that is relevant to professionals, and the url of said article 
                """),
            async_execution=True,
            agent=agent
        )


    @crew
    def crew(self) -> Crew:
        """Creates the DementiaResearchCrew crew"""
        
        analyst = self.dementia_news_analyst()
        reporter = self.dementia_news_reporter()
        
        research_task = self.dementia_news_research_task(analyst)
        report_task = self.dementia_news_report_task(reporter)

        return Crew(
            agents=[analyst, reporter], 
            tasks=[research_task, report_task], 
            process=Process.sequential,
            verbose=True,
        )

def run_crew():
    inputs = {
        'topic': 'Dementia Research Articles',
        'current_year': str(datetime.now().year)
    }
    try:
        dementia_instance = DementiaResearchCrew()
        curr_crew = dementia_instance.crew()
           
        for agent in curr_crew.agents:
            agent.llm = my_ai
            
        curr_crew.kickoff(inputs=inputs)
    except Exception as e:
        raise Exception(f"An error occurred while running the crew : {e}")
        
def main():
    run_crew()

if __name__ == "__main__":
    main()
