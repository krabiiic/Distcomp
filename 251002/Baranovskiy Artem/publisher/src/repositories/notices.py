from src.models.models import Notice
from src.utils.repository import InMemoryRepository, SQLAlchemyRepository


class NoticesRepository(SQLAlchemyRepository):
    model = Notice