pip install fastapi uvicorn sqlalchemy psycopg2 pydantic cassandra-driver

uvicorn main:app --host 127.0.0.1 --port 24110 --reload
uvicorn discussion:app --host 0.0.0.0 --port 24130 --reload


