from fastapi import APIRouter, Depends
from typing import List, Annotated

from src.api.v1.dependencies import get_stickers_service
from src.schemas.stickers import StickerResponseTo, StickerRequestToUpdate
from src.schemas.stickers import StickerRequestToAdd
from src.services.stickers import StickersService

router = APIRouter(prefix="/stickers")


@router.get("", response_model=List[StickerResponseTo])
async def get_stickers(stickers_service: Annotated[StickersService, Depends(get_stickers_service)]):
    return await stickers_service.get_stickers()


@router.get("/{sticker_id}", response_model=StickerResponseTo)
async def get_sticker_by_id(sticker_id: int,
                            stickers_service: Annotated[StickersService, Depends(get_stickers_service)]):
    return await stickers_service.get_sticker_by_id(sticker_id)


@router.post("", response_model=StickerResponseTo, status_code=201)
async def create_sticker(sticker: StickerRequestToAdd,
                         stickers_service: Annotated[StickersService, Depends(get_stickers_service)]):
    sticker_response = await stickers_service.create_sticker(sticker)
    return sticker_response


@router.delete("/{sticker_id}", status_code=204)
async def delete_sticker(sticker_id: int,
                         stickers_service: Annotated[StickersService, Depends(get_stickers_service)]):
    await stickers_service.delete_sticker(sticker_id)
    return sticker_id


@router.put("", response_model=StickerResponseTo)
async def update_sticker(sticker: StickerRequestToUpdate,
                         stickers_service: Annotated[StickersService, Depends(get_stickers_service)]):
    return await stickers_service.update_sticker(sticker)

