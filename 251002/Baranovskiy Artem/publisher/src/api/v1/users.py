from fastapi import APIRouter, Depends
from typing import List, Annotated

from src.api.v1.dependencies import get_users_service
from src.schemas.users import UserResponseTo, UserRequestToUpdate
from src.schemas.users import UserRequestToAdd
from src.services.users import UsersService

router = APIRouter(prefix="/users")


@router.get("", response_model=List[UserResponseTo])
async def get_users(users_service: Annotated[UsersService, Depends(get_users_service)]):
    return await users_service.get_users()


@router.get("/{user_id}", response_model=UserResponseTo)
async def get_user_by_id(user_id: int,
                            users_service: Annotated[UsersService, Depends(get_users_service)]):
    return await users_service.get_user_by_id(user_id)


@router.post("", response_model=UserResponseTo, status_code=201)
async def create_user(user: UserRequestToAdd,
                         users_service: Annotated[UsersService, Depends(get_users_service)]):
    user_response = await users_service.create_user(user)
    return user_response


@router.delete("/{user_id}", status_code=204)
async def delete_user(user_id: int,
                         users_service: Annotated[UsersService, Depends(get_users_service)]):
    await users_service.delete_user(user_id)
    return user_id


@router.put("", response_model=UserResponseTo)
async def update_user(user: UserRequestToUpdate,
                         users_service: Annotated[UsersService, Depends(get_users_service)]):
    return await users_service.update_user(user)

