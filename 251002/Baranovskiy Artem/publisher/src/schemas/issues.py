from datetime import datetime

from pydantic import BaseModel, Field

class IssueBase(BaseModel):
    title: str = Field(min_length=2, max_length=64)
    content: str = Field(min_length=4, max_length=2048)
    user_id: int = Field(alias="userId")

    model_config = {
        'from_attributes': True,
        'populate_by_name': True,
    }


class IssueRequestToAdd(IssueBase):
    pass

class IssueRequestToUpdate(IssueRequestToAdd):
    id: int

class IssueResponseTo(IssueBase):
    id: int
    created: datetime
    modified: datetime
