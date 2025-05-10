from sqlalchemy.orm import Mapped, mapped_column, validates, relationship
from sqlalchemy import Text
from .base import Base
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from .article import Article


class Writer(Base):

    __tablename__ = "writers"

    login: Mapped[str] = mapped_column(Text, nullable = True, unique = True)
    password: Mapped[str] = mapped_column(Text, nullable = True)
    firstname: Mapped[str] = mapped_column(Text, nullable = True)
    lastname: Mapped[str] = mapped_column(Text, nullable = True)


    articles: Mapped[list["Article"]] = relationship(back_populates="writer")

    @validates('login')
    def validate_login(self, key, value):
        if not (2 <= len(value) <= 64):
            raise ValueError("login" + " must be between 2 and 64 characters")
        return value
    

    @validates('firstname')
    def validate_login(self, key, value):
        if not (2 <= len(value) <= 64):
            raise ValueError("firstname" + " must be between 2 and 64 characters")
        return value
    
    @validates('lastname')
    def validate_login(self, key, value):
        if not (2 <= len(value) <= 64):
            raise ValueError("lastname" + " must be between 2 and 64 characters")
        return value
    

        
    @validates('password')
    def validate_login(self, key, value):
        if not (8 <= len(value) <= 128):
            raise ValueError("password" + " must be between 8 and 128 characters")
        return value


