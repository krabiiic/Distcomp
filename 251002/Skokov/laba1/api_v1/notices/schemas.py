from pydantic import BaseModel, ConfigDict


class Notice(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    articleId: int
    content: str


class NoticeID(Notice):
    id: int