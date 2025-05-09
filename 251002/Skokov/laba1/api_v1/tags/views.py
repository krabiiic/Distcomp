from fastapi import APIRouter, status, HTTPException
from .schemas import Tag, TagID
from loguru import logger
from api_v1.util import clear_storage


logger.add(
        sink = "RV1Lab.log",
        mode="w",
        encoding="utf-8",
        format="{time} {level} {notice}",)


router = APIRouter()
prefix = "/tags"

current_tag = {
    "id": 0,
    "name": "",
}


@router.get(prefix + "/{get_id}",
            status_code=status.HTTP_200_OK,
            response_model=TagID)
async def tag_by_id(
    get_id: int
):
    global current_tag
    logger.info(f"GET tag by id: {get_id}")
    if current_tag["id"] != get_id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such tag"
        )
    return TagID.model_validate(current_tag)


@router.get(prefix,
            status_code=status.HTTP_200_OK,
            response_model=TagID)
async def tag():
    global current_tag
    logger.info("GET tag")
    return TagID.model_validate(current_tag)




@router.post(prefix, 
             status_code=status.HTTP_201_CREATED,
             response_model=TagID)
async def create_tag(
    tag: Tag
):
    global current_tag
    logger.info(f"POST tag with body: {tag.model_dump()}")
    current_tag = {"id":0, **tag.model_dump() }

    return TagID.model_validate(current_tag)




@router.delete(prefix + "/{delete_id}",
               status_code=status.HTTP_204_NO_CONTENT)
async def delete_tag(
    delete_id: int
):
    global current_tag
    logger.info(f"DELETE tag with ID: {delete_id}")
    if current_tag["id"] != delete_id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such tag")
    
    current_tag = clear_storage(current_tag)
    return 



@router.put(prefix,
            status_code=status.HTTP_200_OK,
            response_model=TagID)
async def put_writer(
    tag: TagID
):
    global current_tag
    logger.info(f"PUT tag with body: {tag.model_dump()}")
    # if notice. == 'x':
    #         raise HTTPException(
    #         status_code=status.HTTP_400_BAD_REQUEST,
    #         detail="Invlaid PUT data")
    current_tag = {**tag.model_dump()}
    return tag