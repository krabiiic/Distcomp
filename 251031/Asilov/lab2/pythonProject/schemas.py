from pydantic import BaseModel, Field
from typing import List


# DTO модели для пользователей
class UserCreate(BaseModel):
    login: str = Field(..., min_length=4, max_length=64)
    password: str = Field(..., min_length=8)
    firstname: str = Field(..., min_length=3)
    lastname: str = Field(..., min_length=3)

class UserResponse(BaseModel):
    id: int
    login: str
    firstname: str
    lastname: str

    class Config:
        orm_mode = True

class UserUpdate(BaseModel):
    id: int
    login: str = Field(..., min_length=4, max_length=64)
    password: str = Field(..., min_length=8)
    firstname: str = Field(..., min_length=3)
    lastname: str = Field(..., min_length=3)

    class Config:
        orm_mode = True

# DTO модели для задач
class IssueCreate(BaseModel):
    title: str
    content: str
    user_id: int = Field(..., alias="userId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True


class IssueResponse(BaseModel):
    id: int
    title: str
    content: str
    user_id: int = Field(..., alias="userId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True

class IssueUpdate(BaseModel):
    id: int
    title: str
    content: str
    user_id: int = Field(..., alias="userId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True

# DTO модели для реакций
class ReactionCreate(BaseModel):
    content: str
    issue_id: int = Field(..., alias="issueId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True

class ReactionResponse(BaseModel):
    id: int
    content: str
    issue_id: int = Field(..., alias="issueId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True

class ReactionUpdate(BaseModel):
    id: int
    content: str
    issue_id: int = Field(..., alias="issueId")

    class Config:
        allow_population_by_field_name = True
        orm_mode = True


# DTO модели для тегов
class TagCreate(BaseModel):
    # Требуем минимум 2 символа и максимум 20 символов
    name: str = Field(..., min_length=2, max_length=20)

class TagResponse(BaseModel):
    id: int
    name: str

    class Config:
        orm_mode = True

class TagUpdate(BaseModel):
    id: int
    name: str = Field(..., min_length=2, max_length=20)

    class Config:
        orm_mode = True
