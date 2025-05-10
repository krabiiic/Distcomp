from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy import Text, ForeignKey
from .base import Base
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from .article import Article



class Notice(Base):

    __tablename__ = "notices"

    articleId: Mapped[int] = mapped_column(
        ForeignKey("articles.id")
    )

    article: Mapped["Article"] = relationship(back_populates="notices")

    content: Mapped[str] = mapped_column(Text, nullable = True)





