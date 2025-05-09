from fastapi import APIRouter
from .articles.views import router as articles_router 
from .writers.views import router as writers_router
from .notices.views import router as notices_router
from .tags.views import router as tags_router

router = APIRouter(
    prefix="/v1.0"
)

router.include_router(articles_router, tags=["Articles"])
router.include_router(writers_router, tags=["Writers"])
router.include_router(notices_router, tags=["Notices"])
router.include_router(tags_router, tags=["Tags"])
