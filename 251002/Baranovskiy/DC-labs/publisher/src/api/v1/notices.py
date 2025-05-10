from fastapi import APIRouter, Depends
from typing import List, Annotated
from src.api.v1.dependencies import get_notices_service
from src.services.notices import NoticesService


from src.schemas.notices import NoticeResponseTo, NoticeRequestToAdd, NoticeRequestToUpdate


router = APIRouter(prefix="/notices")

@router.get("", response_model=List[NoticeResponseTo])
async def get_notices(notices_service: Annotated[NoticesService, Depends(get_notices_service)]):
    return await notices_service.get_notices()


@router.get("/{notice_id}", response_model=NoticeResponseTo)
async def get_notice_by_id(notice_id: int, notices_service: Annotated[NoticesService, Depends(get_notices_service)]):
    return await notices_service.get_notice_by_id(notice_id)

@router.post("", response_model=NoticeResponseTo, status_code=201)
async def create_notice(notice: NoticeRequestToAdd, notices_service: Annotated[NoticesService, Depends(get_notices_service)]):
    return await notices_service.create_notice(notice)

@router.delete("/{notice_id}", status_code=204)
async def delete_notice(notice_id: int, notices_service: Annotated[NoticesService, Depends(get_notices_service)]):
    await notices_service.delete_notice(notice_id)
    return notice_id

@router.put("", response_model=NoticeResponseTo)
async def update_notice(notice: NoticeRequestToUpdate, notices_service: Annotated[NoticesService, Depends(get_notices_service)]):
    return await notices_service.update_notice(notice)
