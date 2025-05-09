from fastapi import APIRouter, status, HTTPException
from .schemas import Notice, NoticeID
from loguru import logger
from api_v1.util import clear_storage

logger.add(
        sink = "RV1Lab.log",
        mode="w",
        encoding="utf-8",
        format="{time} {level} {notice}",)

router = APIRouter()
prefix = "/notices"


current_notice = {
    "id": 0,
    "articleId": 0,
    "content": "",
}


@router.get(prefix + "/{get_id}",
            status_code=status.HTTP_200_OK,
            response_model=NoticeID)
async def notice_by_id(
    get_id: int
):
    global current_notice
    logger.info(f"GET notice by id: {get_id}")
    if current_notice["id"] != get_id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such notice"
        )
    return NoticeID.model_validate(current_notice)


@router.get(prefix,
            status_code=status.HTTP_200_OK,
            response_model=NoticeID)
async def notice():
    global current_notice
    logger.info("GET notice")
    return NoticeID.model_validate(current_notice)



@router.post(prefix, 
             status_code=status.HTTP_201_CREATED,
             response_model=NoticeID)
async def create_notice(
    notice: Notice
):
    global current_notice
    logger.info(f"POST notice with body: {notice.model_dump()}")
    current_notice = {"id":0, **notice.model_dump() }

    return NoticeID.model_validate(current_notice)




@router.delete(prefix + "/{delete_id}",
               status_code=status.HTTP_204_NO_CONTENT)
async def delete_notice(
    delete_id: int
):
    global current_notice
    logger.info(f"DELETE notice with ID: {delete_id}")
    if current_notice["id"] != delete_id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such notice")

    
    current_notice = clear_storage(current_notice)
    current_notice["articleId"] = 100000
    return 



@router.put(prefix,
            status_code=status.HTTP_200_OK,
            response_model=NoticeID)
async def put_writer(
    notice: NoticeID
):
    global current_notice
    logger.info(f"PUT notice with body: {notice.model_dump()}")
    # if notice. == 'x':
    #         raise HTTPException(
    #         status_code=status.HTTP_400_BAD_REQUEST,
    #         detail="Invlaid PUT data")

    current_notice = {**notice.model_dump()}
    return notice