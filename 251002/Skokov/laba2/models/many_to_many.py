from sqlalchemy import Table, ForeignKey, Column
from .base import Base



Sticker_story  = Table(
    "tbl_storiesticker",
    Base.metadata,
    Column("storyId", ForeignKey("tbl_story.id")),
    Column("stickerId", ForeignKey("tbl_sticker.id")),
)



