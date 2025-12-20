import pyparseit
import json

def read_ai_articles(file_name:str):
    md_json = open(file_name,"r")
    markdown_string = md_json.read()
    md_json.close()
    my_snippets = pyparseit.parse_markdown_string(markdown_string, language='json')
    for i,snippet in enumerate(my_snippets):
        json_data = json.loads(snippet.content)
        json_str = json.dumps(json_data,indent=4)
        with open(f"../centralCode/data/article_{i+1}.json","w") as new_json:
            new_json.write(json_str)


def main():
    read_ai_articles("./output/report.md")

if __name__ == "__main__":
    main()
