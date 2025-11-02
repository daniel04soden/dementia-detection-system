from crewai import Agent, Crew, Process, Task
from crewai.project import CrewBase, agent, crew, task
from crewai.agents.agent_builder.base_agent import BaseAgent
from typing import List

@CrewBase
class DementiaResearchCrew():
    """DementiaResearchCrew crew"""

    agents: List[BaseAgent]
    tasks: List[Task]

    @agent
    def dementia_news_reporter(self) -> Agent:
        return Agent(
            config=self.agents_config['dementia_news_reporter'],
            verbose=True
        )

    @agent
    def dementia_news_analyst(self) -> Agent:
        return Agent(
            config=self.agents_config['dementia_news_analyst'],
            verbose=True
        )
    @task
    def dementia_news_report_task(self) -> Task:
        return Task(
            config=self.tasks_config['dementia_news_report_task'],
            output_file='output/report.md'
        )

    @task  
    def dementia_news_research_task(self) -> Task:
        return Task(
            config=self.tasks_config['dementia_news_research_task'],
            output_file='output/research.md'
        )

    @crew
    def crew(self) -> Crew:
        """Creates the DementiaResearchCrew crew"""

        return Crew(
            agents=self.agents, 
            tasks=self.tasks, 
            process=Process.sequential,
            verbose=True,
            output_file="out.json"
        )
