from src.models.models import Mark
from src.utils.repository import SQLAlchemyRepository


class MarksRepository(SQLAlchemyRepository):
    model = Mark