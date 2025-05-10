from src.models.models import Sticker
from src.utils.repository import SQLAlchemyRepository


class StickersRepository(SQLAlchemyRepository):
    model = Sticker