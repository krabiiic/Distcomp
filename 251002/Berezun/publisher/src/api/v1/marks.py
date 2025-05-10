from fastapi import APIRouter, Depends
from typing import List, Annotated

from src.api.v1.dependencies import get_marks_service
from src.schemas.marks import MarkResponseTo, MarkRequestToUpdate
from src.schemas.marks import MarkRequestToAdd
from src.services.marks import MarksService

router = APIRouter(prefix="/marks")


@router.get("", response_model=List[MarkResponseTo])
async def get_marks(marks_service: Annotated[MarksService, Depends(get_marks_service)]):
    return await marks_service.get_marks()


@router.get("/{mark_id}", response_model=MarkResponseTo)
async def get_mark_by_id(mark_id: int,
                            marks_service: Annotated[MarksService, Depends(get_marks_service)]):
    return await marks_service.get_mark_by_id(mark_id)


@router.post("", response_model=MarkResponseTo, status_code=201)
async def create_mark(mark: MarkRequestToAdd,
                         marks_service: Annotated[MarksService, Depends(get_marks_service)]):
    mark_response = await marks_service.create_mark(mark)
    return mark_response


@router.delete("/{mark_id}", status_code=204)
async def delete_mark(mark_id: int,
                         marks_service: Annotated[MarksService, Depends(get_marks_service)]):
    await marks_service.delete_mark(mark_id)
    return mark_id


@router.put("", response_model=MarkResponseTo)
async def update_mark(mark: MarkRequestToUpdate,
                         marks_service: Annotated[MarksService, Depends(get_marks_service)]):
    return await marks_service.update_mark(mark)

