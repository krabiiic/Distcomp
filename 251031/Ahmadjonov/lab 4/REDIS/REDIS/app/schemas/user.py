from pydantic import BaseModel
from typing import Optional

class UserBase(BaseModel):
    email: str  # Было EmailStr
    username: str

class UserCreate(UserBase):
    password: str

class User(UserBase):
    id: int
    is_active: Optional[bool] = True

    class Config:
        from_attributes = True