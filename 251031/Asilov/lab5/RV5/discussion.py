from fastapi import FastAPI
from pydantic import BaseModel
from cassandra.cluster import Cluster
from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model
from cassandra.cqlengine.connection import register_connection, set_default_connection
from cassandra.cqlengine.management import sync_table
import time
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def connect_to_cassandra():
    max_retries = 10
    retry_delay = 10  # секунды
    for attempt in range(max_retries):
        try:
            # Попробуй host.docker.internal или IP контейнера
            cluster = Cluster(['host.docker.internal'], port=9042)
            # Альтернатива: cluster = Cluster(['172.17.0.2'], port=9042)
            session = cluster.connect('my_keyspace')
            logger.info("Successfully connected to Cassandra")
            return cluster, session
        except Exception as e:
            logger.error(f"Failed to connect to Cassandra (attempt {attempt + 1}/{max_retries}): {e}")
            if attempt < max_retries - 1:
                time.sleep(retry_delay)
    raise Exception("Could not connect to Cassandra after multiple attempts")

CLUSTER, SESSION = connect_to_cassandra()
register_connection('default', session=SESSION)
set_default_connection('default')

class Reaction(Model):
    __keyspace__ = 'my_keyspace'
    __table_name__ = 'tbl_reaction'
    id = columns.Integer(primary_key=True)
    issue_id = columns.Integer()
    content = columns.Text()

try:
    sync_table(Reaction)
    logger.info("Table tbl_reaction synced successfully")
except Exception as e:
    logger.error(f"Failed to sync table: {e}")
    raise

app = FastAPI()

class ReactionCreate(BaseModel):
    issue_id: int
    content: str

@app.post("/api/v1.0/reactions", status_code=201)
def create_reaction(reaction: ReactionCreate):
    try:
        max_id_query = SESSION.execute("SELECT MAX(id) FROM tbl_reaction")
        max_id = max_id_query.one()[0] or 0
        new_id = max_id + 1

        new_reaction = Reaction.create(
            id=new_id,
            issue_id=reaction.issue_id,
            content=reaction.content
        )
        logger.info(f"Reaction created with id {new_id}")
        return {"message": "Reaction created", "id": new_reaction.id}
    except Exception as e:
        logger.error(f"Error creating reaction: {e}")
        raise

@app.get("/api/v1.0/reactions")
def get_reactions():
    try:
        reactions = Reaction.objects.all()
        return [
            {"id": r.id, "issue_id": r.issue_id, "content": r.content}
            for r in reactions
        ]
    except Exception as e:
        logger.error(f"Error fetching reactions: {e}")
        raise
