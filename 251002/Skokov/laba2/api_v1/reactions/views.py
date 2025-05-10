from fastapi import APIRouter, status, HTTPException, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from loguru import logger

from db_helper import db_helper

from .schemas import Reaction, ReactionID
import api_v1.reactions.crud as crud
from .helper import check_story


logger.add(
    sink="RV2Lab.log",
    mode="w",
    encoding="utf-8",
    format="{time} {level} {reaction}",
)

router = APIRouter(
    prefix="/reactions",
)

global costyl_id
costyl_id = 0


@router.get("/{get_id}", status_code=status.HTTP_200_OK, response_model=ReactionID)
async def reaction_by_id(
    get_id: int, session: AsyncSession = Depends(db_helper.session_dependency)
):
    logger.info(f"GET definite Reaction with id: {get_id}")
    reaction = await crud.get_reaction(session=session, reaction_id=get_id)
    if not reaction:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No such reaction"
        )
    return reaction


@router.get("", status_code=status.HTTP_200_OK, response_model=ReactionID)
async def reaction(session: AsyncSession = Depends(db_helper.session_dependency)):
    logger.info("GET Reaction")
    global costyl_id

    reaction = await crud.get_reaction(session=session, reaction_id=costyl_id)
    if not reaction:
        return {
            "id": 0,
            "storyId": 0,
            "content": " " * 2,
        }
    return reaction


@router.post("", status_code=status.HTTP_201_CREATED, response_model=ReactionID)
async def create_reaction(
    reaction_info: Reaction, session: AsyncSession = Depends(db_helper.session_dependency)
):
    logger.info(f"POST Reaction with body: {reaction_info.model_dump()}")

    await check_story(session=session, story_id=reaction_info.storyId)

    reaction = await crud.create_reaction(session=session, reaction_info=reaction_info)

    if not reaction:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST, detail="Incorrect data"
        )

    global costyl_id
    costyl_id = reaction.id
    return reaction


@router.delete("/{delete_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_reaction(
    delete_id: int, session: AsyncSession = Depends(db_helper.session_dependency)
):

    logger.info(f"DELETE reaction with ID: {delete_id}")
    delete_state = await crud.delete_reaction(reaction_id=delete_id, session=session)
    if not delete_state:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No such Reaction"
        )
    return


@router.put("", status_code=status.HTTP_200_OK, response_model=ReactionID)
async def put_writer(
    reaction: ReactionID, session: AsyncSession = Depends(db_helper.session_dependency)
):
    logger.info(f"PUT Reaction with body: {reaction.model_dump()}")
    await check_story(session=session, story_id=reaction.storyId)
    reaction = await crud.put_reaction(reaction_info=reaction, session=session)
    if not reaction:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST, detail="Invlaid PUT data"
        )
    return reaction
