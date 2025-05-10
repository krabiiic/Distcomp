from fastapi import HTTPException

from src.models.models import Mark
from src.schemas.marks import MarkRequestToAdd, MarkResponseTo, MarkRequestToUpdate
from src.utils.repository import AbstractRepository, NotFoundError


class MarksService:
    def __init__(self, marks_repo: AbstractRepository):
        self.marks_repo: AbstractRepository = marks_repo

    async def create_mark(self, mark: MarkRequestToAdd) -> MarkResponseTo:
        mark_model = Mark(
            name=mark.name
        )

        try:
            created_model = await self.marks_repo.create(mark_model)
            return MarkResponseTo.model_validate(created_model)
        except ValueError as e:
            raise HTTPException(status_code=403, detail=str(e))

    async def get_marks(self) -> list[MarkResponseTo]:
        marks = await self.marks_repo.get_all()
        return [MarkResponseTo.model_validate(mark) for mark in marks]

    async def get_mark_by_id(self, mark_id: int) -> MarkResponseTo:
        try:
            mark = await self.marks_repo.get_by_id(mark_id)
            return MarkResponseTo.model_validate(mark)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Mark not found")

    async def delete_mark(self, mark_id: int):
        try:
            await self.marks_repo.delete(mark_id)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Mark not found")

    async def update_mark(self, mark: MarkRequestToUpdate) -> MarkResponseTo:
        try:
            existing_mark = await self.marks_repo.get_by_id(mark.id)

            existing_mark.name = mark.name

            updated_mark = await self.marks_repo.update(existing_mark)
            return MarkResponseTo.model_validate(updated_mark)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Mark not found")