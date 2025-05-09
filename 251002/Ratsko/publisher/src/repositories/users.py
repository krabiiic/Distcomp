from src.models.models import User
from src.utils.repository import InMemoryRepository, SQLAlchemyRepository


class UsersRepository(SQLAlchemyRepository):
    model = User