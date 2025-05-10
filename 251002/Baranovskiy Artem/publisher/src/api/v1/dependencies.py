from fastapi import Depends

from src.repositories.users import UsersRepository
from src.repositories.stickers import StickersRepository
from src.repositories.notices import NoticesRepository
from src.repositories.issues import IssuesRepository
from src.services.users import UsersService
from src.services.stickers import StickersService
from src.services.notices import NoticesService
from src.services.issues import IssuesService
from src.utils.redis_client import redis_client

users_repository = UsersRepository()
issues_repository = IssuesRepository()
notices_repository = NoticesRepository()
stickers_repository = StickersRepository()


def get_users_service():
    return UsersService(users_repository)

def  get_issues_service():
    return IssuesService(issues_repository)

def get_notices_service():
    return NoticesService(redis_client)

def get_stickers_service():
    return StickersService(stickers_repository)
