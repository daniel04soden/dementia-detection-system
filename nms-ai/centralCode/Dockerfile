FROM ubuntu:24.04

RUN apt-get update && apt-get install -y \
	python3 \
	python3-pip \
	python3-venv \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /app

RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY *.py ./

COPY model_store/ ./model_store/
COPY ADReSS-IS2020-data-train/ ./ADReSS-IS2020-data-train/
COPY ADReSS-IS2020-data-test/ ./ADReSS-IS2020-data-test/
COPY data/ ./data/

EXPOSE 5000

CMD ["python", "web.py"]
