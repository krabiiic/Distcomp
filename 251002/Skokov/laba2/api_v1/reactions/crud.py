from sqlalchemy import Result, select
from .schemas import Reaction as ReactionIN, ReactionID
from sqlalchemy.ext.asyncio import AsyncSession
from models import Reaction



async def create_reaction(
        reaction_info: ReactionIN,
        session: AsyncSession
):
    reaction = Reaction(**reaction_info.model_dump())
    session.add(reaction)
    await session.commit()
    return reaction



async def get_reaction(
        session: AsyncSession,      
        reaction_id: int
):
    stat = select(Reaction).where(Reaction.id == reaction_id)
    result: Result = await session.execute(stat)
    # writers: Sequence = result.scalars().all()
    reaction: Reaction | None = result.scalar_one_or_none()
    return reaction


# async def get_reaction_by_title(
#         session: AsyncSession,      
#         reaction_title: str
# ):
#     stat = select(reaction).where(reaction.title == reaction_title)
#     result: Result = await session.execute(stat)
#     # writers: Sequence = result.scalars().all()
#     reaction: reaction | None = result.scalar_one_or_none()

#     return reaction


async def delete_reaction(
        reaction_id: int,
        session: AsyncSession
):
    reaction = await get_reaction(reaction_id=reaction_id, session=session)
    if not reaction:
        return False
    await session.delete(reaction)
    await session.commit()
    return True


async def put_reaction(
        reaction_info: ReactionID,
        session: AsyncSession
):
    reaction_id = reaction_info.id
    reaction_update = ReactionIN(**reaction_info.model_dump())
    reaction = await get_reaction(reaction_id=reaction_id, session=session)
    if not reaction:
        return False
    
    for name, value in reaction_update.model_dump().items():
        setattr(reaction, name, value)
    await session.commit()
    return reaction