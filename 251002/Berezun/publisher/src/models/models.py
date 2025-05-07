from datetime import datetime, UTC
from typing import List, Optional
from sqlalchemy import ForeignKey, String, Text, DateTime, Table, Column, Integer
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship

from src.db.db import Base


class Creator(Base):
    __tablename__ = 'tbl_creator'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    login: Mapped[str] = mapped_column(String(64), unique=True, nullable=False)
    password: Mapped[str] = mapped_column(String(128), nullable=False)
    firstname: Mapped[str] = mapped_column(String(64), nullable=False)
    lastname: Mapped[str] = mapped_column(String(64), nullable=False)

    news: Mapped[List["News"]] = relationship(back_populates="creator")


class News(Base):
    __tablename__ = 'tbl_news'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    creator_id: Mapped[int] = mapped_column(ForeignKey("tbl_creator.id"), nullable=False)
    title: Mapped[str] = mapped_column(String(64), unique=True, nullable=False)
    content: Mapped[str] = mapped_column(Text, nullable=False)
    created: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=lambda: datetime.now(UTC),
        nullable=False
    )
    modified: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=lambda: datetime.now(UTC),
        onupdate=lambda: datetime.now(UTC),
        nullable=False
    )

    creator: Mapped["Creator"] = relationship(back_populates="news")
    reactions: Mapped[List["Reaction"]] = relationship(back_populates="news")
    marks: Mapped[List["Mark"]] = relationship(
        secondary="tbl_news_mark",
        back_populates="news"
    )


class Reaction(Base):
    __tablename__ = 'tbl_reaction'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    newsId: Mapped[int] = mapped_column(ForeignKey("tbl_news.id"), nullable=False)
    content: Mapped[str] = mapped_column(Text, nullable=False)

    news: Mapped["News"] = relationship(back_populates="reactions")


class Mark(Base):
    __tablename__ = 'tbl_mark'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String(32), unique=True, nullable=False)

    news: Mapped[List["News"]] = relationship(
        secondary="tbl_news_mark",
        back_populates="marks"
    )


tbl_news_mark = Table(
    'tbl_news_mark',
    Base.metadata,
    Column('newsId', ForeignKey('tbl_news.id'), primary_key=True),
    Column('markId', ForeignKey('tbl_mark.id'), primary_key=True)
)