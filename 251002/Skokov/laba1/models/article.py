from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy import Text, ForeignKey, DateTime, func
from .base import Base
from typing import TYPE_CHECKING
from datetime import datetime

from sqlalchemy import Table, ForeignKey, Column

if TYPE_CHECKING:
    from .writer import Writer
    from .notices import Notice



association_table = Table(
    "association_table",
    Base.metadata,
    Column("article_id", ForeignKey("articles.id")),
    Column("tag_id", ForeignKey("tags.id")),
)


class Article(Base):

    __tablename__ = "articles"

    writerId: Mapped[int] = mapped_column(
        ForeignKey("writers.id")
    )
    writer: Mapped["Writer"] = relationship(back_populates="articles")



    title: Mapped[str] = mapped_column(Text, nullable = True)
    content: Mapped[str] = mapped_column(Text, nullable = True)
    created: Mapped[str] = mapped_column(Text, nullable = True)
    modified: Mapped[datetime] = mapped_column(DateTime, server_default=func.now(), default=datetime.now,)

    notices: Mapped[list["Notice"]] = relationship(back_populates="article")


    # many to many with tag
    tags: Mapped[list["Tag"]] = relationship(secondary=association_table)



class Tag(Base):

    __tablename__ = "tags"

    name: Mapped[str] = mapped_column(Text, nullable = True)


    # many to many with article
    articles: Mapped[list["Article"]] = relationship(secondary=association_table)



