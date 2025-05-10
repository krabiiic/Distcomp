python kafka_consumer.py

uvicorn main:app --host 127.0.0.1 --port 24110 --reload
uvicorn discussion:app --host 0.0.0.0 --port 24130 --reload
pip install fastapi uvicorn sqlalchemy psycopg2 pydantic cassandra-driver confluent-kafka redis


