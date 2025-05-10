from sqlalchemy.ext.asyncio import AsyncSession
from fastapi import HTTPException, status
from api_v1.stories.crud import get_story

async def check_story(
        story_id: int,
        session: AsyncSession
):
    """
    We can't connect Reaction with defunct Story
    """
    story_exists = await get_story(story_id=story_id, session=session)
    if not story_exists:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Story doesn't exist")