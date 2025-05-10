from .schemas import Story as StoryIN, StoryBD, StoryID
from models import Story
from sqlalchemy.ext.asyncio import AsyncSession
from fastapi import HTTPException, status
import api_v1.stories.crud as crud
from api_v1.writers.crud import get_writer


async def check_title(
    title: str,
    session: AsyncSession
):
    """
    Title must be unique 
    """
    title_exists = await crud.get_story_by_title(
        story_title=title,
        session=session
    )
    if title_exists:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Title already exists")
    

async def check_writer(
        writer_id: int,
        session: AsyncSession
):
    """
    We can't connect Story with defunct Writer
    """
    writer_exists = await get_writer(writer_id=writer_id, session=session)
    if not writer_exists:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Writer doesn't exist")

def story_to_bd(
        story: StoryIN
) -> StoryBD:
    return StoryBD(
        content= story.content,
        writer_id= story.writerId,
        title= story.title,
    )


def bd_to_id(
        story: Story
) -> StoryID:
    return StoryID(
        content=story.content,
        writerId=story.writer_id,
        id=story.id,
        title=story.title,
    )