from fastapi import APIRouter, HTTPException, status
from loguru import logger
from .schemas import Article, ArticleID
from api_v1.util import clear_storage


logger.add(
        sink = "RV1Lab.log",
        mode="w",
        encoding="utf-8",
        format="{time} {level} {notice}",)

router = APIRouter()
prefix = "/articles"

current_article = {
    "id": 0,
    "writerId": 0,
    "title": "",
    "content": "",
}



@router.get(prefix + "/{get_id}",
            status_code=status.HTTP_200_OK,
            response_model=ArticleID)
async def article_by_id(
    get_id: int
):
    global current_article
    logger.info(f"GET article by id {get_id}")
    if get_id != current_article["id"]:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such article"
        )
    return ArticleID.model_validate(current_article)


@router.get(prefix,
            status_code=status.HTTP_200_OK,
            response_model=ArticleID)
async def article():
    global current_article
    logger.info("GET article")
    return ArticleID.model_validate(current_article)


@router.post(prefix,
            status_code= status.HTTP_201_CREATED,
            response_model=ArticleID)
async def create_article(
    article: Article
):
    global current_article
    logger.info(f"POST writer with body: {article.model_dump()}")
    current_article = {"id":0, **article.model_dump() }
    return ArticleID.model_validate(current_article)


@router.delete(prefix + "/{delete_id}",
               status_code=status.HTTP_204_NO_CONTENT)
async def delete_article(
    delete_id: int
):
    global current_article
    logger.info(f"DELETE writer with ID: {delete_id}")
    if delete_id != current_article["id"]:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such article"
        )
    current_article = clear_storage(current_article)
    current_article["writerId"] = 100000
    return 


@router.put(prefix,
            status_code=status.HTTP_200_OK,
            response_model=ArticleID)
async def put_article(
    article: ArticleID
):
    global current_article
    logger.info(f"PUT writer with body: {article.model_dump()}")
    if article.title == "x":
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invlaid PUT data"
        )
    current_article = {**article.model_dump()}
    return article

