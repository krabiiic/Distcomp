from datetime import datetime, UTC
from typing import List, Optional
from sqlalchemy import ForeignKey, String, Text, DateTime, Table, Column, Integer
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship

from src.db.db import Base


class User(Base):
    __tablename__ = 'tbl_user'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    login: Mapped[str] = mapped_column(String(64), unique=True, nullable=False)
    password: Mapped[str] = mapped_column(String(128), nullable=False)
    firstname: Mapped[str] = mapped_column(String(64), nullable=False)
    lastname: Mapped[str] = mapped_column(String(64), nullable=False)

    issues: Mapped[List["Issue"]] = relationship(back_populates="user")


class Issue(Base):
    __tablename__ = 'tbl_issue'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    user_id: Mapped[int] = mapped_column(ForeignKey("tbl_user.id"), nullable=False)
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

    user: Mapped["User"] = relationship(back_populates="issues")
    notices: Mapped[List["Notice"]] = relationship(back_populates="issue")
    stickers: Mapped[List["Sticker"]] = relationship(
        secondary="tbl_issue_sticker",
        back_populates="issues"
    )


class Notice(Base):
    __tablename__ = 'tbl_notice'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    issueId: Mapped[int] = mapped_column(ForeignKey("tbl_issue.id"), nullable=False)
    content: Mapped[str] = mapped_column(Text, nullable=False)

    issue: Mapped["Issue"] = relationship(back_populates="notices")


class Sticker(Base):
    __tablename__ = 'tbl_sticker'

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String(32), unique=True, nullable=False)

    issues: Mapped[List["Issue"]] = relationship(
        secondary="tbl_issue_sticker",
        back_populates="stickers"
    )


tbl_issue_sticker = Table(
    'tbl_issue_sticker',
    Base.metadata,
    Column('issueId', ForeignKey('tbl_issue.id'), primary_key=True),
    Column('stickerId', ForeignKey('tbl_sticker.id'), primary_key=True)
)