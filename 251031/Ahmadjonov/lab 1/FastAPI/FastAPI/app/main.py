from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from typing import List, Optional
from sqlalchemy import create_engine, Column, Integer, String, Text, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship, Session
from urllib.parse import quote_plus

# Исправленная строка подключения с кодированием пароля
password = "0413120"
encoded_password = quote_plus(password)
DATABASE_URL = f"postgresql+psycopg2://postgres:{encoded_password}@localhost:5432/distcomp"

# Создаем engine с дополнительными параметрами для надежности
engine = create_engine(
    DATABASE_URL,
    pool_pre_ping=True,  # Проверяет соединение перед использованием
    pool_size=10,  # Размер пула соединений
    max_overflow=20,  # Максимальное количество соединений сверх pool_size
    echo=True  # Логирование SQL запросов для отладки
)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

app = FastAPI()

# --- SQLAlchemy Models ---

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    login = Column(String, unique=True, index=True, nullable=False)
    password = Column(String, nullable=False)
    firstname = Column(String, nullable=False)
    lastname = Column(String, nullable=False)
    news = relationship("News", back_populates="user", cascade="all, delete-orphan")


class News(Base):
    __tablename__ = "news"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
    title = Column(String, nullable=False)
    content = Column(Text, nullable=False)
    user = relationship("User", back_populates="news")
    reactions = relationship("Reaction", back_populates="news", cascade="all, delete-orphan")


class Reaction(Base):
    __tablename__ = "reactions"
    id = Column(Integer, primary_key=True, index=True)
    news_id = Column(Integer, ForeignKey("news.id", ondelete="CASCADE"), nullable=False)
    content = Column(Text, nullable=False)
    news = relationship("News", back_populates="reactions")


class Tag(Base):
    __tablename__ = "tags"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, nullable=False)


# Создаем таблицы (если они еще не существуют)
try:
    Base.metadata.create_all(bind=engine)
    print("Таблицы успешно созданы")
except Exception as e:
    print(f"Ошибка при создании таблиц: {e}")


# --- Pydantic Models ---

class UserBase(BaseModel):
    login: str
    firstname: str
    lastname: str


class UserCreate(UserBase):
    password: str


class UserResponse(UserBase):
    id: int

    class Config:
        from_attributes = True  # Заменено orm_mode на from_attributes в Pydantic v2


class NewsBase(BaseModel):
    title: str
    content: str


class NewsCreate(NewsBase):
    user_id: int


class NewsResponse(NewsBase):
    id: int
    user_id: int

    class Config:
        from_attributes = True


class ReactionBase(BaseModel):
    content: str


class ReactionCreate(ReactionBase):
    news_id: int


class ReactionResponse(ReactionBase):
    id: int
    news_id: int

    class Config:
        from_attributes = True


class TagBase(BaseModel):
    name: str


class TagCreate(TagBase):
    pass


class TagResponse(TagBase):
    id: int

    class Config:
        from_attributes = True


# Dependency для получения сессии БД
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# --- API Endpoints ---

@app.post("/api/v1.0/users", response_model=UserResponse, status_code=201)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    # Проверка на существующего пользователя
    db_user = db.query(User).filter(User.login == user.login).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Login already registered")

    new_user = User(
        login=user.login,
        password=user.password,  # В реальном приложении нужно хешировать пароль
        firstname=user.firstname,
        lastname=user.lastname
    )
    db.add(new_user)
    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(new_user)
    return new_user


@app.get("/api/v1.0/users", response_model=List[UserResponse])
def get_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = db.query(User).offset(skip).limit(limit).all()
    return users


@app.get("/api/v1.0/users/{user_id}", response_model=UserResponse)
def get_user(user_id: int, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


@app.put("/api/v1.0/users/{user_id}", response_model=UserResponse)
def update_user(user_id: int, user: UserCreate, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.id == user_id).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="User not found")

    # Проверка на уникальность логина
    if user.login != db_user.login:
        existing_user = db.query(User).filter(User.login == user.login).first()
        if existing_user:
            raise HTTPException(status_code=400, detail="Login already in use")

    db_user.login = user.login
    db_user.password = user.password  # В реальном приложении нужно хешировать пароль
    db_user.firstname = user.firstname
    db_user.lastname = user.lastname

    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_user)
    return db_user


@app.delete("/api/v1.0/users/{user_id}", status_code=204)
def delete_user(user_id: int, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.id == user_id).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="User not found")

    try:
        db.delete(db_user)
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    return None


# --- News Endpoints ---

@app.post("/api/v1.0/news", response_model=NewsResponse, status_code=201)
def create_news(news: NewsCreate, db: Session = Depends(get_db)):
    # Проверка существования пользователя
    user = db.query(User).filter(User.id == news.user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    db_news = News(
        title=news.title,
        content=news.content,
        user_id=news.user_id
    )
    db.add(db_news)
    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_news)
    return db_news


@app.get("/api/v1.0/news", response_model=List[NewsResponse])
def get_all_news(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    news = db.query(News).offset(skip).limit(limit).all()
    return news


@app.get("/api/v1.0/news/{news_id}", response_model=NewsResponse)
def get_news(news_id: int, db: Session = Depends(get_db)):
    news = db.query(News).filter(News.id == news_id).first()
    if not news:
        raise HTTPException(status_code=404, detail="News not found")
    return news


@app.put("/api/v1.0/news/{news_id}", response_model=NewsResponse)
def update_news(news_id: int, news: NewsBase, db: Session = Depends(get_db)):  # Изменено на NewsBase
    db_news = db.query(News).filter(News.id == news_id).first()
    if not db_news:
        raise HTTPException(status_code=404, detail="News not found")

    db_news.title = news.title
    db_news.content = news.content

    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_news)
    return db_news


@app.delete("/api/v1.0/news/{news_id}", status_code=204)
def delete_news(news_id: int, db: Session = Depends(get_db)):
    news = db.query(News).filter(News.id == news_id).first()
    if not news:
        raise HTTPException(status_code=404, detail="News not found")

    try:
        db.delete(news)
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    return None


# --- Reaction Endpoints ---

@app.post("/api/v1.0/reactions", response_model=ReactionResponse, status_code=201)
def create_reaction(reaction: ReactionCreate, db: Session = Depends(get_db)):
    # Проверка существования новости
    news = db.query(News).filter(News.id == reaction.news_id).first()
    if not news:
        raise HTTPException(status_code=404, detail="News not found")

    db_reaction = Reaction(
        content=reaction.content,
        news_id=reaction.news_id
    )
    db.add(db_reaction)
    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_reaction)
    return db_reaction


@app.get("/api/v1.0/reactions", response_model=List[ReactionResponse])
def get_all_reactions(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    reactions = db.query(Reaction).offset(skip).limit(limit).all()
    return reactions


@app.get("/api/v1.0/reactions/{reaction_id}", response_model=ReactionResponse)
def get_reaction(reaction_id: int, db: Session = Depends(get_db)):
    reaction = db.query(Reaction).filter(Reaction.id == reaction_id).first()
    if not reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")
    return reaction


@app.put("/api/v1.0/reactions/{reaction_id}", response_model=ReactionResponse)
def update_reaction(reaction_id: int, reaction: ReactionBase, db: Session = Depends(get_db)):  # Изменено на ReactionBase
    db_reaction = db.query(Reaction).filter(Reaction.id == reaction_id).first()
    if not db_reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")

    db_reaction.content = reaction.content

    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_reaction)
    return db_reaction


@app.delete("/api/v1.0/reactions/{reaction_id}", status_code=204)
def delete_reaction(reaction_id: int, db: Session = Depends(get_db)):
    reaction = db.query(Reaction).filter(Reaction.id == reaction_id).first()
    if not reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")

    try:
        db.delete(reaction)
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    return None


# --- Tag Endpoints ---

@app.post("/api/v1.0/tags", response_model=TagResponse, status_code=201)
def create_tag(tag: TagCreate, db: Session = Depends(get_db)):
    # Проверка на существующий тег
    db_tag = db.query(Tag).filter(Tag.name == tag.name).first()
    if db_tag:
        raise HTTPException(status_code=400, detail="Tag already exists")

    new_tag = Tag(name=tag.name)
    db.add(new_tag)
    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(new_tag)
    return new_tag


@app.get("/api/v1.0/tags", response_model=List[TagResponse])
def get_all_tags(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    tags = db.query(Tag).offset(skip).limit(limit).all()
    return tags


@app.get("/api/v1.0/tags/{tag_id}", response_model=TagResponse)
def get_tag(tag_id: int, db: Session = Depends(get_db)):
    tag = db.query(Tag).filter(Tag.id == tag_id).first()
    if not tag:
        raise HTTPException(status_code=404, detail="Tag not found")
    return tag


@app.put("/api/v1.0/tags/{tag_id}", response_model=TagResponse)
def update_tag(tag_id: int, tag: TagCreate, db: Session = Depends(get_db)):
    db_tag = db.query(Tag).filter(Tag.id == tag_id).first()
    if not db_tag:
        raise HTTPException(status_code=404, detail="Tag not found")

    # Проверка на уникальность имени тега
    if tag.name != db_tag.name:
        existing_tag = db.query(Tag).filter(Tag.name == tag.name).first()
        if existing_tag:
            raise HTTPException(status_code=400, detail="Tag name already in use")

    db_tag.name = tag.name
    try:
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    db.refresh(db_tag)
    return db_tag


@app.delete("/api/v1.0/tags/{tag_id}", status_code=204)
def delete_tag(tag_id: int, db: Session = Depends(get_db)):
    tag = db.query(Tag).filter(Tag.id == tag_id).first()
    if not tag:
        raise HTTPException(status_code=404, detail="Tag not found")

    try:
        db.delete(tag)
        db.commit()
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    return None