from enum import Enum

from pydantic import BaseModel, Field, AliasChoices, ConfigDict


class ReactionState(str, Enum):
    PENDING = "PENDING"
    APPROVE = "APPROVE"
    DECLINE = "DECLINE"

class ReactionBase(BaseModel):
    newsId: int = Field(validation_alias=AliasChoices('newsId', 'newsid'))
    content: str = Field(min_length=2, max_length=2048)

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
    )

class ReactionRequestToAdd(ReactionBase):
    pass

class ReactionRequestToUpdate(ReactionRequestToAdd):
    id: int

class ReactionResponseTo(ReactionBase):
    id: int
    state: ReactionState
