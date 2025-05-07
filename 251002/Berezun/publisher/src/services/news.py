from fastapi import HTTPException

from src.models.models import News
from src.schemas.news import NewsRequestToAdd, NewsResponseTo, NewsRequestToUpdate
from src.utils.repository import AbstractRepository, NotFoundError


class NewsService:
    def __init__(self, news_repo: AbstractRepository):
        self.news_repo: AbstractRepository = news_repo

    async def create_news(self, news: NewsRequestToAdd) -> NewsResponseTo:
        news_model = News(
            title=news.title,
            content=news.content,
            creator_id=news.creator_id,
        )

        try:
            created_model = await self.news_repo.create(news_model)
            return NewsResponseTo.model_validate(created_model)
        except ValueError as e:
            raise HTTPException(status_code=403, detail=str(e))

    async def get_news(self) -> list[NewsResponseTo]:
        news = await self.news_repo.get_all()
        return [NewsResponseTo.model_validate(news) for news in news]

    async def get_news_by_id(self, news_id: int) -> NewsResponseTo:
        try:
            news = await self.news_repo.get_by_id(news_id)
            return NewsResponseTo.model_validate(news)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="News not found")

    async def delete_news(self, news_id: int):
        try:
            await self.news_repo.delete(news_id)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="News not found")

    async def update_news(self, news: NewsRequestToUpdate) -> NewsResponseTo:
        try:
            existing_news = await self.news_repo.get_by_id(news.id)

            existing_news.title = news.title
            existing_news.content = news.content
            existing_news.creator_id = news.creator_id

            updated_news = await self.news_repo.update(existing_news)
            return NewsResponseTo.model_validate(updated_news)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="News not found")