from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from database import Base


# Модели БД
class UserDB(Base):
    __tablename__ = "tbl_user"
    id = Column(Integer, primary_key=True, index=True)
    login = Column(String, unique=True, nullable=False)
    password = Column(String, nullable=False)
    firstname = Column(String, nullable=False)
    lastname = Column(String, nullable=False)

    issues = relationship("IssueDB", back_populates="user")

class IssueDB(Base):
    __tablename__ = "tbl_issue"
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    content = Column(String, nullable=False)
    user_id = Column(Integer, ForeignKey("tbl_user.id"), nullable=False)

    user = relationship("UserDB", back_populates="issues")
    reactions = relationship("ReactionDB", back_populates="issue")

class ReactionDB(Base):
    __tablename__ = "tbl_reaction"
    id = Column(Integer, primary_key=True, index=True)
    content = Column(String, nullable=False)
    issue_id = Column(Integer, ForeignKey("tbl_issue.id"), nullable=False)

    issue = relationship("IssueDB", back_populates="reactions")

class TagDB(Base):
    __tablename__ = "tbl_tag"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, nullable=False)
