from fastapi import Depends

from src.repositories.creators import CreatorsRepository
from src.repositories.marks import MarksRepository
from src.repositories.reactions import ReactionsRepository
from src.repositories.news import NewsRepository
from src.services.creators import CreatorsService
from src.services.marks import MarksService
from src.services.reactions import ReactionsService
from src.services.news import NewsService
from src.utils.redis_client import redis_client

creators_repository = CreatorsRepository()
news_repository = NewsRepository()
reactions_repository = ReactionsRepository()
marks_repository = MarksRepository()


def get_creators_service():
    return CreatorsService(creators_repository)

def  get_news_service():
    return NewsService(news_repository)

def get_reactions_service():
    return ReactionsService(redis_client)

def get_marks_service():
    return MarksService(marks_repository)
