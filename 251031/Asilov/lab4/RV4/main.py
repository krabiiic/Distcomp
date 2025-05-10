import logging
from fastapi import FastAPI, HTTPException, Depends, Response
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session
from typing import List
from schemas import UserResponse, ReactionResponse, ReactionCreate, TagResponse, IssueResponse, IssueCreate, \
    IssueUpdate, UserUpdate, UserCreate, ReactionUpdate, TagCreate, TagUpdate
from database import engine, Base, get_db
from models import UserDB, IssueDB, ReactionDB, TagDB
from kafka_producer import send_to_kafka

# Настройка логирования
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Инициализация FastAPI
app = FastAPI(docs_url='/docs')

# Создание таблиц (для тестового окружения – удаляем старые и создаем новые)
Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)


@app.post("/api/v1.0/users", response_model=UserResponse, status_code=201)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    existing_user = db.query(UserDB).filter(UserDB.login == user.login).first()
    if existing_user:
        logger.error(f"User with login {user.login} already exists.")
        raise HTTPException(status_code=403, detail="User with this login already exists")
    db_user = UserDB(
        login=user.login,
        password=user.password,
        firstname=user.firstname,
        lastname=user.lastname
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    logger.info(f"User created with id {db_user.id}")
    return db_user


@app.get("/api/v1.0/users", response_model=List[UserResponse])
def list_users(db: Session = Depends(get_db)):
    return db.query(UserDB).all()


@app.get("/api/v1.0/users/{user_id}", response_model=UserResponse)
def get_user(user_id: int, db: Session = Depends(get_db)):
    db_user = db.query(UserDB).filter(UserDB.id == user_id).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@app.put("/api/v1.0/users", response_model=UserResponse)
def update_user(user: UserUpdate, db: Session = Depends(get_db)):
    db_user = db.query(UserDB).filter(UserDB.id == user.id).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="User not found")
    db_user.login = user.login
    db_user.password = user.password
    db_user.firstname = user.firstname
    db_user.lastname = user.lastname
    db.commit()
    db.refresh(db_user)
    return db_user


from sqlalchemy.exc import SQLAlchemyError, IntegrityError


@app.delete("/api/v1.0/users/{user_id}", status_code=204)
def delete_user(user_id: int, db: Session = Depends(get_db)):
    # Ищем пользователя по id
    db_user = db.query(UserDB).filter(UserDB.id == user_id).first()
    if not db_user:
        logger.error(f"Пользователь с id {user_id} не найден.")
        raise HTTPException(status_code=404, detail="Пользователь не найден")

    try:
        # Удаляем связанные задачи (Issues)
        deleted_issues = db.query(IssueDB).filter(IssueDB.user_id == user_id).delete(synchronize_session=False)
        logger.info(f"Удалено связанных задач для пользователя {user_id}: {deleted_issues} записей.")

        # Удаляем пользователя
        db.delete(db_user)
        db.commit()
        logger.info(f"Пользователь с id {user_id} успешно удалён.")
    except IntegrityError as e:
        db.rollback()
        logger.error(f"Ошибка целостности данных при удалении пользователя с id {user_id}: {e}")
        raise HTTPException(status_code=400, detail="Ошибка целостности данных: имеются связанные записи")
    except SQLAlchemyError as e:
        db.rollback()
        logger.error(f"Ошибка базы данных при удалении пользователя с id {user_id}: {e}")
        raise HTTPException(status_code=500, detail="Ошибка базы данных")
    except Exception as e:
        db.rollback()
        logger.error(f"Непредвиденная ошибка при удалении пользователя с id {user_id}: {e}")
        raise HTTPException(status_code=500, detail="Внутренняя ошибка сервера")

    return


# CRUD для задач
# Функция для отправки задачи в Kafka
@app.post("/api/v1.0/issues", response_model=IssueResponse, status_code=201)
def create_issue(issue: IssueCreate, db: Session = Depends(get_db)):
    try:
        # Проверяем, существует ли пользователь
        db_user = db.query(UserDB).filter(UserDB.id == issue.user_id).first()
        if not db_user:
            raise HTTPException(status_code=400, detail="User not found")

        # Создаем задачу в БД
        db_issue = IssueDB(title=issue.title, content=issue.content, user_id=issue.user_id)
        db.add(db_issue)
        db.commit()
        db.refresh(db_issue)

        # Отправляем сообщение в Kafka (например, для уведомления о новой задаче)
        send_to_kafka("task_created", f"New task created: {db_issue.id}, Title: {db_issue.title}")

        return db_issue

    except Exception as e:
        logging.error(f"Error creating issue: {str(e)}")
        raise HTTPException(status_code=500, detail="Internal server error")


@app.get("/api/v1.0/issues/{issue_id}", response_model=IssueResponse)
def get_issue(issue_id: int, db: Session = Depends(get_db)):
    db_issue = db.query(IssueDB).filter(IssueDB.id == issue_id).first()
    if not db_issue:
        raise HTTPException(status_code=404, detail="Issue not found")
    return db_issue


@app.get("/api/v1.0/issues", response_model=List[IssueResponse])
def list_issues(db: Session = Depends(get_db)):
    try:
        return db.query(IssueDB).all()
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")


@app.put("/api/v1.0/issues/{issue_id}", response_model=IssueResponse)
def update_issue(issue_id: int, issue: IssueUpdate, db: Session = Depends(get_db)):
    db_issue = db.query(IssueDB).filter(IssueDB.id == issue_id).first()
    if not db_issue:
        logger.error(f"Issue with id {issue_id} not found.")
        raise HTTPException(status_code=404, detail="Issue not found")

    # Обновление задачи
    db_issue.title = issue.title
    db_issue.content = issue.content
    db.commit()
    db.refresh(db_issue)
    return db_issue


@app.delete("/api/v1.0/issues/{issue_id}", status_code=204)
def delete_issue(issue_id: str, db: Session = Depends(get_db)):
    try:
        numeric_issue_id = int(issue_id)
    except ValueError:
        logger.error(f"Некорректный id задачи: {issue_id}. Считаем, что задача отсутствует.")
        return Response(status_code=204)

    db_issue = db.query(IssueDB).filter(IssueDB.id == numeric_issue_id).first()
    if not db_issue:
        logger.info(f"Задача с id {numeric_issue_id} не найдена. Возвращаем 204.")
        return Response(status_code=204)

    db.delete(db_issue)
    db.commit()
    return Response(status_code=204)



# CRUD для реакций
@app.post("/api/v1.0/reactions", response_model=ReactionResponse, status_code=201)
def create_reaction(reaction: ReactionCreate, db: Session = Depends(get_db)):
    db_issue = db.query(IssueDB).filter(IssueDB.id == reaction.issue_id).first()
    if not db_issue:
        raise HTTPException(status_code=400, detail="Issue not found")
    db_reaction = ReactionDB(
        content=reaction.content,
        issue_id=reaction.issue_id
    )
    db.add(db_reaction)
    db.commit()
    db.refresh(db_reaction)
    return db_reaction



@app.get("/api/v1.0/reactions", response_model=List[ReactionResponse])
def list_reactions(db: Session = Depends(get_db)):
    return db.query(ReactionDB).all()


@app.get("/api/v1.0/reactions/{reaction_id}", response_model=ReactionResponse)
def get_reaction(reaction_id: int, db: Session = Depends(get_db)):
    db_reaction = db.query(ReactionDB).filter(ReactionDB.id == reaction_id).first()
    if not db_reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")
    return db_reaction


@app.put("/api/v1.0/reactions", response_model=ReactionResponse)
def update_reaction(reaction: ReactionUpdate, db: Session = Depends(get_db)):
    db_reaction = db.query(ReactionDB).filter(ReactionDB.id == reaction.id).first()
    if not db_reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")
    db_issue = db.query(IssueDB).filter(IssueDB.id == reaction.issue_id).first()
    if not db_issue:
        raise HTTPException(status_code=400, detail="Issue not found")
    db_reaction.content = reaction.content
    db_reaction.issue_id = reaction.issue_id
    db.commit()
    db.refresh(db_reaction)
    return db_reaction


@app.delete("/api/v1.0/reactions/{reaction_id}", status_code=204)
def delete_reaction(reaction_id: int, db: Session = Depends(get_db)):
    db_reaction = db.query(ReactionDB).filter(ReactionDB.id == reaction_id).first()
    if not db_reaction:
        raise HTTPException(status_code=404, detail="Reaction not found")
    db.delete(db_reaction)
    db.commit()
    return


# CRUD для тегов
@app.post("/api/v1.0/tags", response_model=TagResponse, status_code=201)
def create_tag(tag: TagCreate, db: Session = Depends(get_db)):
    existing_tag = db.query(TagDB).filter(TagDB.name == tag.name).first()
    if existing_tag:
        raise HTTPException(status_code=403, detail="Tag with this name already exists")
    db_tag = TagDB(name=tag.name)
    db.add(db_tag)
    db.commit()
    db.refresh(db_tag)
    return db_tag


@app.get("/api/v1.0/tags", response_model=List[TagResponse])
def list_tags(db: Session = Depends(get_db)):
    return db.query(TagDB).all()


@app.get("/api/v1.0/tags/{tag_id}", response_model=TagResponse)
def get_tag(tag_id: int, db: Session = Depends(get_db)):
    db_tag = db.query(TagDB).filter(TagDB.id == tag_id).first()
    if not db_tag:
        raise HTTPException(status_code=404, detail="Tag not found")
    return db_tag


@app.put("/api/v1.0/tags", response_model=TagResponse)
def update_tag(tag: TagUpdate, db: Session = Depends(get_db)):
    db_tag = db.query(TagDB).filter(TagDB.id == tag.id).first()
    if not db_tag:
        raise HTTPException(status_code=404, detail="Tag not found")
    db_tag.name = tag.name
    db.commit()
    db.refresh(db_tag)
    return db_tag


@app.delete("/api/v1.0/tags/{tag_id}", status_code=204)
def delete_tag(tag_id: int, db: Session = Depends(get_db)):
    db_tag = db.query(TagDB).filter(TagDB.id == tag_id).first()
    if not db_tag:
        raise HTTPException(status_code=404, detail="Tag not found")
    db.delete(db_tag)
    db.commit()
    return



