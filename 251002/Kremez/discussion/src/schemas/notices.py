from enum import Enum

from pydantic import BaseModel, Field, AliasChoices, ConfigDict


class NoticeState(str, Enum):
    PENDING = "PENDING"
    APPROVE = "APPROVE"
    DECLINE = "DECLINE"

class NoticeBase(BaseModel):
    issueId: int = Field(validation_alias=AliasChoices('issueId', 'issueid'))
    content: str = Field(min_length=2, max_length=2048)

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
    )

class NoticeRequestToAdd(NoticeBase):
    pass

class NoticeRequestToUpdate(NoticeRequestToAdd):
    id: int

class NoticeResponseTo(NoticeBase):
    id: int
    state: NoticeState
