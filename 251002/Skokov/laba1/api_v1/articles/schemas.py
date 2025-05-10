from pydantic import BaseModel, ConfigDict


class Article(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    writerId: int
    title: str
    content: str 


class ArticleID(Article):
    id: int