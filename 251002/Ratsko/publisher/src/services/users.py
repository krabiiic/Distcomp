from fastapi import HTTPException

from src.models.models import User
from src.schemas.users import UserRequestToAdd, UserResponseTo, UserRequestToUpdate
from src.utils.repository import AbstractRepository, NotFoundError


class UsersService:
    def __init__(self, users_repo: AbstractRepository):
        self.users_repo: AbstractRepository = users_repo

    async def create_user(self, user: UserRequestToAdd) -> UserResponseTo:
        user_model = User(
            login=user.login,
            password=user.password,
            firstname=user.firstname,
            lastname=user.lastname
        )

        try:
            created_model = await self.users_repo.create(user_model)
            return UserResponseTo.model_validate(created_model)
        except ValueError as e:
            raise HTTPException(status_code=403, detail=str(e))

    async def get_users(self) -> list[UserResponseTo]:
        users = await self.users_repo.get_all()
        return [UserResponseTo.model_validate(user) for user in users]

    async def get_user_by_id(self, user_id: int) -> UserResponseTo:
        try:
            user = await self.users_repo.get_by_id(user_id)
            return UserResponseTo.model_validate(user)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="User not found")

    async def delete_user(self, user_id: int):
        try:
            await self.users_repo.delete(user_id)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="User not found")

    async def update_user(self, user: UserRequestToUpdate) -> UserResponseTo:
        try:
            existing_user = await self.users_repo.get_by_id(user.id)

            existing_user.login = user.login
            existing_user.password = user.password
            existing_user.firstname = user.firstname
            existing_user.lastname = user.lastname

            updated_user = await self.users_repo.update(existing_user)
            return UserResponseTo.model_validate(updated_user)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="User not found")