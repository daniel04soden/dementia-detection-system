import json
from openai import OpenAI
import os
from dotenv import load_dotenv
from openai.types.chat import ChatCompletionMessageParam


def main():
    load_dotenv()
    client = OpenAI(
        api_key=f"{os.getenv('MY_KEY')}", base_url="https://api.deepseek.com"
    )
    system_prompt = """
    You have two roles:
    You're a meticulous reporter with a considerate approach to reporting on news. You're known for
    your ability to turn complex data and hard news stories into easy to digest and consise summaries while retaining the facts, making
    it easy for both patients and meidcal professionals to understand the information you are putting out there.

    EXAMPLE JSON OUTPUT:
    [
    {
        "headline":"[insert headline]" 
        "snippet": "[insert description of article found]",
        "url": "https://[insert remaining url]"
    },
    ]

    """

    user_prompt = """
    Create twenty json objects based on dementia news sources and research findings both for patients and normal civillians to use, as well as for meidcal professionals and researchers to use.
    The first 10 should be for patients and the second 10 should be for professionals to view. Make sure to use real articles with real urls.
    """

    message: list[ChatCompletionMessageParam] = [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": user_prompt},
    ]

    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=message,
        response_format={"type": "json_object"},
    )

    content_str = response.choices[0].message.content
    if content_str:
        data = json.loads(content_str)
        with open("json_articles.json", "w") as json_file:
            json.dump(data, json_file, indent=4)

    else:
        print("Unknown format, not json")


if __name__ == "__main__":
    main()
