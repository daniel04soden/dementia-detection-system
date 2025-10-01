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
    def dementia_patient_news_reporter(self) -> Agent:
        return Agent(
            config=self.agents_config['dementia_patient_news_reporter'],
            verbose=True
        )

    @agent
    def dementia_professional_news_reporter(self) -> Agent:
        return Agent(
            config=self.agents_config['dementia_professional_news_reporter'],
            verbose=True
        )
    @task
    def patient_news_research_task(self) -> Task:
        return Task(
            config=self.tasks_config['patient_news_research_task'],
            output_file='output/patient_report.md'
        )

    @task  
    def professional_news_research_task(self) -> Task:
        return Task(
            config=self.tasks_config['professional_news_research_task'],
            output_file='output/professional_report.md'
        )

    @crew
    def crew(self) -> Crew:
        """Creates the DementiaResearchCrew crew"""

        return Crew(
            agents=self.agents, 
            tasks=self.tasks, 
            process=Process.sequential,
            verbose=True,
        )
