from pydantic import BaseModel, Field

from src.models.models import User


class UserBase(BaseModel):
    login: str = Field(min_length=2, max_length=64)
    firstname: str = Field(min_length=2, max_length=64)
    lastname: str = Field(min_length=2, max_length=64)

    model_config = {
        'from_attributes': True,
    }

class UserRequestToAdd(UserBase):
    password: str = Field(min_length=8, max_length=128)

class UserRequestToUpdate(UserRequestToAdd):
    id: int

class UserResponseTo(UserBase):
    id: int
