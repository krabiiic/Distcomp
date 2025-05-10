from fastapi import HTTPException

from src.models.models import Sticker
from src.schemas.stickers import StickerRequestToAdd, StickerResponseTo, StickerRequestToUpdate
from src.utils.repository import AbstractRepository, NotFoundError


class StickersService:
    def __init__(self, stickers_repo: AbstractRepository):
        self.stickers_repo: AbstractRepository = stickers_repo

    async def create_sticker(self, sticker: StickerRequestToAdd) -> StickerResponseTo:
        sticker_model = Sticker(
            name=sticker.name
        )

        try:
            created_model = await self.stickers_repo.create(sticker_model)
            return StickerResponseTo.model_validate(created_model)
        except ValueError as e:
            raise HTTPException(status_code=403, detail=str(e))

    async def get_stickers(self) -> list[StickerResponseTo]:
        stickers = await self.stickers_repo.get_all()
        return [StickerResponseTo.model_validate(sticker) for sticker in stickers]

    async def get_sticker_by_id(self, sticker_id: int) -> StickerResponseTo:
        try:
            sticker = await self.stickers_repo.get_by_id(sticker_id)
            return StickerResponseTo.model_validate(sticker)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Sticker not found")

    async def delete_sticker(self, sticker_id: int):
        try:
            await self.stickers_repo.delete(sticker_id)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Sticker not found")

    async def update_sticker(self, sticker: StickerRequestToUpdate) -> StickerResponseTo:
        try:
            existing_sticker = await self.stickers_repo.get_by_id(sticker.id)

            existing_sticker.name = sticker.name

            updated_sticker = await self.stickers_repo.update(existing_sticker)
            return StickerResponseTo.model_validate(updated_sticker)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Sticker not found")