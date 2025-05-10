from pydantic import BaseModel, ConfigDict


class Tag(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    name: str


class TagID(Tag):
    id: int