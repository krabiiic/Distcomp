from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model
from cassandra.cqlengine.management import sync_table
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
from cassandra.io.asyncioreactor import AsyncioConnection  # Исправление для Python 3.12
from redis import Redis
from kafka import KafkaProducer
import json
import uuid
from datetime import datetime
import os
import logging


# Настройка логгирования
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI
app = FastAPI()

# Configuration
CASSANDRA_HOSTS = os.getenv('CASSANDRA_HOSTS', 'localhost').split(',')
CASSANDRA_PORT = int(os.getenv('CASSANDRA_PORT', 9042))
CASSANDRA_USER = os.getenv('CASSANDRA_USER', 'cassandra')
CASSANDRA_PASSWORD = os.getenv('CASSANDRA_PASSWORD', 'cassandra')
REDIS_HOST = os.getenv('REDIS_HOST', 'localhost')
REDIS_PORT = int(os.getenv('REDIS_PORT', 6379))
KAFKA_BOOTSTRAP_SERVERS = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:9092').split(',')

# Initialize Cassandra with AsyncioConnection for Python 3.12
try:
    auth_provider = PlainTextAuthProvider(username=CASSANDRA_USER, password=CASSANDRA_PASSWORD)
    cluster = Cluster(
        contact_points=CASSANDRA_HOSTS,
        port=CASSANDRA_PORT,
        auth_provider=auth_provider,
        connection_class=AsyncioConnection  # Ключевое исправление
    )
    session = cluster.connect()

    # Create keyspace if not exists
    session.execute("""
        CREATE KEYSPACE IF NOT EXISTS microservice
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}
    """)
    session.set_keyspace('microservice')
    logger.info("Successfully connected to Cassandra")
except Exception as e:
    logger.error(f"Failed to connect to Cassandra: {e}")
    raise

# Initialize Redis
try:
    redis = Redis(host=REDIS_HOST, port=REDIS_PORT, db=0, decode_responses=True)
    redis.ping()  # Проверка подключения
    logger.info("Successfully connected to Redis")
except Exception as e:
    logger.error(f"Failed to connect to Redis: {e}")
    raise

# Initialize Kafka Producer
try:
    kafka_producer = KafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_serializer=lambda v: json.dumps(v).encode('utf-8'),
        retries=3
    )
    logger.info("Successfully connected to Kafka")
except Exception as e:
    logger.error(f"Failed to initialize Kafka producer: {e}")
    kafka_producer = None


# Database models
class User(Model):
    __table_name__ = 'users'
    id = columns.UUID(primary_key=True, default=uuid.uuid4)
    username = columns.Text(required=True)
    email = columns.Text(required=True)
    created_at = columns.DateTime(default=datetime.now)


class News(Model):
    __table_name__ = 'news'
    id = columns.UUID(primary_key=True, default=uuid.uuid4)
    title = columns.Text(required=True)
    content = columns.Text(required=True)
    author_id = columns.UUID(required=True)
    created_at = columns.DateTime(default=datetime.now)
    tags = columns.List(columns.Text)


class Reaction(Model):
    __table_name__ = 'reactions'
    id = columns.UUID(primary_key=True, default=uuid.uuid4)
    news_id = columns.UUID(required=True)
    user_id = columns.UUID(required=True)
    reaction_type = columns.Text(required=True)
    created_at = columns.DateTime(default=datetime.now)


class Tag(Model):
    __table_name__ = 'tags'
    id = columns.UUID(primary_key=True, default=uuid.uuid4)
    name = columns.Text(required=True, index=True)
    news_count = columns.Integer(default=0)


# Sync Cassandra tables
try:
    sync_table(User)
    sync_table(News)
    sync_table(Reaction)
    sync_table(Tag)
    logger.info("Successfully synced Cassandra tables")
except Exception as e:
    logger.error(f"Failed to sync Cassandra tables: {e}")


# Pydantic models
class UserCreate(BaseModel):
    username: str
    email: str


class UserResponse(BaseModel):
    id: str
    username: str
    email: str
    created_at: datetime


class NewsCreate(BaseModel):
    title: str
    content: str
    author_id: str
    tags: List[str] = []


class NewsResponse(BaseModel):
    id: str
    title: str
    content: str
    author_id: str
    created_at: datetime
    tags: List[str]


class ReactionCreate(BaseModel):
    news_id: str
    user_id: str
    reaction_type: str


class ReactionResponse(BaseModel):
    id: str
    news_id: str
    user_id: str
    reaction_type: str
    created_at: datetime


class TagCreate(BaseModel):
    name: str


class TagResponse(BaseModel):
    id: str
    name: str
    news_count: int


# Helper functions
def publish_to_kafka(topic: str, message: dict):
    if kafka_producer is None:
        logger.warning("Kafka producer not initialized, skipping message")
        return
    try:
        future = kafka_producer.send(topic, message)
        future.get(timeout=10)  # Wait for confirmation
        logger.info(f"Successfully published message to {topic}")
    except Exception as e:
        logger.error(f"Failed to publish message to Kafka: {e}")


def get_redis_key(entity: str, entity_id: str) -> str:
    return f"{entity}:{entity_id}"


# User endpoints
@app.post("/users/", response_model=UserResponse, status_code=201)
async def create_user(user: UserCreate):
    try:
        user_id = uuid.uuid4()
        user_data = User.create(
            id=user_id,
            username=user.username,
            email=user.email
        )

        # Cache in Redis
        redis_key = get_redis_key('user', str(user_id))
        user_dict = {
            'id': str(user_id),
            'username': user.username,
            'email': user.email,
            'created_at': datetime.now().isoformat()
        }
        redis.set(redis_key, json.dumps(user_dict), ex=3600)

        publish_to_kafka('user_created', user_dict)
        return user_data
    except Exception as e:
        logger.error(f"Error creating user: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@app.get("/users/{user_id}", response_model=UserResponse)
async def get_user(user_id: str):
    try:
        # Try Redis first
        redis_key = get_redis_key('user', user_id)
        cached_user = redis.get(redis_key)
        if cached_user:
            return json.loads(cached_user)

        # If not in Redis, check Cassandra
        user = User.objects.filter(id=uuid.UUID(user_id)).first()
        if not user:
            raise HTTPException(status_code=404, detail="User not found")

        # Cache in Redis
        user_dict = {
            'id': str(user.id),
            'username': user.username,
            'email': user.email,
            'created_at': user.created_at.isoformat()
        }
        redis.set(redis_key, json.dumps(user_dict), ex=3600)

        return user
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid user ID format")
    except Exception as e:
        logger.error(f"Error getting user {user_id}: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@app.delete("/users/{user_id}")
async def delete_user(user_id: str):
    try:
        user = User.objects.filter(id=uuid.UUID(user_id)).first()
        if not user:
            raise HTTPException(status_code=404, detail="User not found")

        user.delete()
        publish_to_kafka('user_deleted', {'id': user_id})

        # Invalidate Redis cache
        redis.delete(get_redis_key('user', user_id))

        return {"message": "User deleted successfully"}
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid user ID format")
    except Exception as e:
        logger.error(f"Error deleting user {user_id}: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


# News endpoints
@app.post("/news/", response_model=NewsResponse, status_code=201)
async def create_news(news: NewsCreate):
    try:
        news_id = uuid.uuid4()
        news_data = News.create(
            id=news_id,
            title=news.title,
            content=news.content,
            author_id=uuid.UUID(news.author_id),
            tags=news.tags
        )

        # Update tag counts
        for tag_name in news.tags:
            tag = Tag.objects.filter(name=tag_name).first()
            if tag:
                tag.news_count += 1
                tag.save()
            else:
                Tag.create(name=tag_name, news_count=1)

        publish_to_kafka('news_created', {
            'id': str(news_id),
            'title': news.title,
            'author_id': news.author_id,
            'created_at': datetime.now().isoformat()
        })

        return news_data
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid author ID format")
    except Exception as e:
        logger.error(f"Error creating news: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@app.get("/news/{news_id}", response_model=NewsResponse)
async def get_news(news_id: str):
    try:
        # Try Redis first
        redis_key = get_redis_key('news', news_id)
        cached_news = redis.get(redis_key)
        if cached_news:
            return json.loads(cached_news)

        news = News.objects.filter(id=uuid.UUID(news_id)).first()
        if not news:
            raise HTTPException(status_code=404, detail="News not found")

        # Cache in Redis
        news_dict = {
            'id': str(news.id),
            'title': news.title,
            'content': news.content,
            'author_id': str(news.author_id),
            'created_at': news.created_at.isoformat(),
            'tags': news.tags
        }
        redis.set(redis_key, json.dumps(news_dict), ex=3600)

        return news
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid news ID format")
    except Exception as e:
        logger.error(f"Error getting news {news_id}: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


# Health check endpoint
@app.get("/health")
async def health_check():
    services = {
        "cassandra": False,
        "redis": False,
        "kafka": False
    }

    try:
        session.execute("SELECT release_version FROM system.local")
        services["cassandra"] = True
    except:
        pass

    try:
        services["redis"] = redis.ping()
    except:
        pass

    services["kafka"] = kafka_producer is not None

    return {
        "status": "OK" if all(services.values()) else "DEGRADED",
        "services": services
    }


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)