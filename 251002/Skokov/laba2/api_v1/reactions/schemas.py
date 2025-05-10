from pydantic import BaseModel, ConfigDict, Field


class Reaction(BaseModel):
    # model_config = ConfigDict(from_attributes=True)
    storyId: int
    content: str = Field(min_length=2, max_length=2048)


class ReactionID(Reaction):
    id: int