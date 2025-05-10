from pydantic import BaseModel, Field

from src.models.models import Sticker


class StickerBase(BaseModel):
    name: str = Field(min_length=2, max_length=32)

    model_config = {
        'from_attributes': True,
    }

class StickerRequestToAdd(StickerBase):
    pass

class StickerRequestToUpdate(StickerRequestToAdd):
    id: int

class StickerResponseTo(StickerBase):
    id: int
