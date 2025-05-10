from src.models.models import Issue
from src.utils.repository import InMemoryRepository, SQLAlchemyRepository


class IssuesRepository(SQLAlchemyRepository):
    model = Issue