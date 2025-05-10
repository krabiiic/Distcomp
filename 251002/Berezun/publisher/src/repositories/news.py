from src.models.models import News
from src.utils.repository import InMemoryRepository, SQLAlchemyRepository


class NewsRepository(SQLAlchemyRepository):
    model = News