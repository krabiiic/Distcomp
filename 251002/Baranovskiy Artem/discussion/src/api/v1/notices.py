
from fastapi import APIRouter, HTTPException

from src.schemas.notices import NoticeResponseTo, NoticeRequestToAdd, NoticeRequestToUpdate
from src.repositories import notices

router = APIRouter(prefix="/api/v1.0/notices", tags=["discussion"])

@router.get("", response_model=list[NoticeResponseTo])
async def get_notices():
    data = notices.get_all_notices()
    return [NoticeResponseTo(**d) for d in data]

@router.get("/{notice_id}", response_model=NoticeResponseTo)
async def get_notice_by_id(notice_id: int):
    row = notices.get_notice_by_id(notice_id)
    if not row:
        raise HTTPException(status_code=404, detail="Notice not found")
    return NoticeResponseTo(**row)

@router.post("", response_model=NoticeResponseTo, status_code=201)
async def create_notice(payload: NoticeRequestToAdd):
    created = notices.create_notice(payload.issueId, payload.content)
    return NoticeResponseTo(**created)

@router.delete("/{notice_id}", status_code=204)
async def delete_notice(notice_id: int):
    row = notices.get_notice_by_id(notice_id)
    if not row:
        raise HTTPException(status_code=404, detail="Notice not found")
    notices.delete_notice(notice_id)
    return

@router.put("", response_model=NoticeResponseTo)
async def update_notice(payload: NoticeRequestToUpdate):
    row = notices.get_notice_by_id(payload.id)
    if not row:
        raise HTTPException(status_code=404, detail="Notice not found")
    updated = notices.update_notice(payload.id, payload.issueId, payload.content, "APPROVE")
    return NoticeResponseTo(**updated)
