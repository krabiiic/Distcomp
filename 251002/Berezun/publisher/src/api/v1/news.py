from fastapi import APIRouter, Depends
from typing import List, Annotated

from src.api.v1.dependencies import get_news_service
from src.schemas.news import NewsResponseTo, NewsRequestToUpdate
from src.schemas.news import NewsRequestToAdd
from src.services.news import NewsService

router = APIRouter(prefix="/news")


@router.get("", response_model=List[NewsResponseTo])
async def get_news(news_service: Annotated[NewsService, Depends(get_news_service)]):
    return await news_service.get_news()


@router.get("/{news_id}", response_model=NewsResponseTo)
async def get_news_by_id(news_id: int,
                            news_service: Annotated[NewsService, Depends(get_news_service)]):
    return await news_service.get_news_by_id(news_id)


@router.post("", response_model=NewsResponseTo, status_code=201)
async def create_news(news: NewsRequestToAdd,
                         news_service: Annotated[NewsService, Depends(get_news_service)]):
    news_response = await news_service.create_news(news)
    return news_response


@router.delete("/{news_id}", status_code=204)
async def delete_news(news_id: int,
                         news_service: Annotated[NewsService, Depends(get_news_service)]):
    await news_service.delete_news(news_id)
    return news_id


@router.put("", response_model=NewsResponseTo)
async def update_news(news: NewsRequestToUpdate,
                         news_service: Annotated[NewsService, Depends(get_news_service)]):
    return await news_service.update_news(news)