from datetime import datetime

from pydantic import BaseModel, Field

class NewsBase(BaseModel):
    title: str = Field(min_length=2, max_length=64)
    content: str = Field(min_length=4, max_length=2048)
    creator_id: int = Field(alias="creatorId")

    model_config = {
        'from_attributes': True,
        'populate_by_name': True,
    }


class NewsRequestToAdd(NewsBase):
    pass

class NewsRequestToUpdate(NewsRequestToAdd):
    id: int

class NewsResponseTo(NewsBase):
    id: int
    created: datetime
    modified: datetime
