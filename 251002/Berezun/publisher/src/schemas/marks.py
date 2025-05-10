from pydantic import BaseModel, Field

from src.models.models import Mark


class MarkBase(BaseModel):
    name: str = Field(min_length=2, max_length=32)

    model_config = {
        'from_attributes': True,
    }

class MarkRequestToAdd(MarkBase):
    pass

class MarkRequestToUpdate(MarkRequestToAdd):
    id: int

class MarkResponseTo(MarkBase):
    id: int
