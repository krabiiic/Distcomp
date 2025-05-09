from fastapi import APIRouter
from .stories.views import router as stories_router 
from .writers.views import router as writers_router
from .reactions.views import router as reactions_router
from .stickers.views import router as stickers_router

router = APIRouter(
    prefix="/v1.0"
)

router.include_router(stories_router, tags=["Stories"])
router.include_router(writers_router, tags=["Writers"])
router.include_router(reactions_router, tags=["Reactions"])
router.include_router(stickers_router, tags=["Stickers"])
