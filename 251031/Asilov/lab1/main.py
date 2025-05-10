from fastapi import FastAPI, HTTPException, status, APIRouter, Request
from fastapi.responses import JSONResponse
from pydantic import BaseModel, Field
from typing import List, Optional, Dict

app = FastAPI(title="REST API", version="1.0")

# Обработчик исключений для формирования ответа с errorMessage и errorCode
@app.exception_handler(HTTPException)
async def http_exception_handler(request: Request, exc: HTTPException):
    return JSONResponse(
        status_code=exc.status_code,
        content={"errorMessage": exc.detail, "errorCode": f"{exc.status_code}01"}
    )

# -------------------------------
# DTO для User с валидацией
# -------------------------------
class UserRequestTo(BaseModel):
    login: str = Field(..., min_length=3)
    password: str = Field(..., min_length=3)
    firstname: str = Field(..., min_length=1)
    lastname: str = Field(..., min_length=1)

class UserResponseTo(BaseModel):
    id: int
    login: str = Field(..., min_length=3)
    password: str = Field(..., min_length=3)
    firstname: str = Field(..., min_length=1)
    lastname: str = Field(..., min_length=1)

# -------------------------------
# DTO для Issue с валидацией полей title и content
# -------------------------------
class IssueRequestTo(BaseModel):
    userId: int
    title: str = Field(..., min_length=3)
    content: str = Field(..., min_length=3)

class IssueResponseTo(BaseModel):
    id: int
    userId: int
    title: str = Field(..., min_length=3)
    content: str = Field(..., min_length=3)

# -------------------------------
# DTO для Tag
# -------------------------------
class TagRequestTo(BaseModel):
    name: str

class TagResponseTo(BaseModel):
    id: int
    name: str

# -------------------------------
# DTO для Reaction
# -------------------------------
class ReactionRequestTo(BaseModel):
    content: str
    issueId: int

class ReactionResponseTo(BaseModel):
    id: int
    content: str
    issueId: int

# -------------------------------
# In‑memory хранилище
# -------------------------------
users_db: Dict[int, UserResponseTo] = {}
issues_db: Dict[int, IssueResponseTo] = {}
tags_db: Dict[int, TagResponseTo] = {}
reactions_db: Dict[int, ReactionResponseTo] = {}

# -------------------------------
# Сервисный слой с генерацией id через счетчики
# -------------------------------
class UserService:
    user_counter = 1

    @staticmethod
    def create(user_req: UserRequestTo) -> UserResponseTo:
        new_id = UserService.user_counter
        UserService.user_counter += 1
        user = UserResponseTo(id=new_id, **user_req.dict())
        users_db[new_id] = user
        return user

    @staticmethod
    def get_all() -> List[UserResponseTo]:
        return list(users_db.values())

    @staticmethod
    def get_by_id(user_id: int) -> UserResponseTo:
        if user_id not in users_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        return users_db[user_id]

    @staticmethod
    def update(user: UserResponseTo) -> UserResponseTo:
        if user.id not in users_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        users_db[user.id] = user
        return user

    @staticmethod
    def delete(user_id: int):
        if user_id not in users_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        del users_db[user_id]

class IssueService:
    issue_counter = 1

    @staticmethod
    def create(issue_req: IssueRequestTo) -> IssueResponseTo:
        if issue_req.userId not in users_db:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="UserId does not exist")
        new_id = IssueService.issue_counter
        IssueService.issue_counter += 1
        issue = IssueResponseTo(id=new_id, **issue_req.dict())
        issues_db[new_id] = issue
        return issue

    @staticmethod
    def get_all() -> List[IssueResponseTo]:
        return list(issues_db.values())

    @staticmethod
    def get_by_id(issue_id: int) -> IssueResponseTo:
        if issue_id not in issues_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Issue not found")
        return issues_db[issue_id]

    @staticmethod
    def update(issue: IssueResponseTo) -> IssueResponseTo:
        if issue.id not in issues_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Issue not found")
        if issue.userId not in users_db:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="UserId does not exist")
        issues_db[issue.id] = issue
        return issue

    @staticmethod
    def delete(issue_id: int):
        if issue_id not in issues_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Issue not found")
        del issues_db[issue_id]

    @staticmethod
    def search(user_login: Optional[str] = None, title: Optional[str] = None, content: Optional[str] = None) -> List[IssueResponseTo]:
        result = list(issues_db.values())
        if user_login:
            result = [i for i in result if i.userId in users_db and users_db[i.userId].login == user_login]
        if title:
            result = [i for i in result if title.lower() in i.title.lower()]
        if content:
            result = [i for i in result if content.lower() in i.content.lower()]
        return result

class TagService:
    tag_counter = 1

    @staticmethod
    def create(tag_req: TagRequestTo) -> TagResponseTo:
        new_id = TagService.tag_counter
        TagService.tag_counter += 1
        tag = TagResponseTo(id=new_id, **tag_req.dict())
        tags_db[new_id] = tag
        return tag

    @staticmethod
    def get_all() -> List[TagResponseTo]:
        return list(tags_db.values())

    @staticmethod
    def get_by_id(tag_id: int) -> TagResponseTo:
        if tag_id not in tags_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tag not found")
        return tags_db[tag_id]

    @staticmethod
    def update(tag: TagResponseTo) -> TagResponseTo:
        if tag.id not in tags_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tag not found")
        tags_db[tag.id] = tag
        return tag

    @staticmethod
    def delete(tag_id: int):
        if tag_id not in tags_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tag not found")
        del tags_db[tag_id]

class ReactionService:
    reaction_counter = 1

    @staticmethod
    def create(reaction_req: ReactionRequestTo) -> ReactionResponseTo:
        if reaction_req.issueId not in issues_db:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="IssueId does not exist")
        new_id = ReactionService.reaction_counter
        ReactionService.reaction_counter += 1
        reaction = ReactionResponseTo(id=new_id, **reaction_req.dict())
        reactions_db[new_id] = reaction
        return reaction

    @staticmethod
    def get_all() -> List[ReactionResponseTo]:
        return list(reactions_db.values())

    @staticmethod
    def get_by_id(reaction_id: int) -> ReactionResponseTo:
        if reaction_id not in reactions_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Reaction not found")
        return reactions_db[reaction_id]

    @staticmethod
    def update(reaction: ReactionResponseTo) -> ReactionResponseTo:
        if reaction.id not in reactions_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Reaction not found")
        if reaction.issueId not in issues_db:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="IssueId does not exist")
        reactions_db[reaction.id] = reaction
        return reaction

    @staticmethod
    def delete(reaction_id: int):
        if reaction_id not in reactions_db:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Reaction not found")
        del reactions_db[reaction_id]

# -------------------------------
# Контроллеры (роутеры)
# -------------------------------
api_prefix = "/api/v1.0"

user_router = APIRouter(prefix="/users", tags=["users"])
issue_router = APIRouter(prefix="/issues", tags=["issues"])
tag_router = APIRouter(prefix="/tags", tags=["tags"])
reaction_router = APIRouter(prefix="/reactions", tags=["reactions"])

# Эндпоинты для User
@user_router.post("", response_model=UserResponseTo, status_code=status.HTTP_201_CREATED)
def create_user(user_req: UserRequestTo):
    return UserService.create(user_req)

@user_router.get("", response_model=List[UserResponseTo])
def get_users():
    return UserService.get_all()

@user_router.get("/{user_id}", response_model=UserResponseTo)
def get_user(user_id: int):
    return UserService.get_by_id(user_id)

@user_router.put("", response_model=UserResponseTo)
def update_user(user: UserResponseTo):
    if not user.id:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="User id is required for update")
    return UserService.update(user)

@user_router.delete("/{user_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_user(user_id: str):
    try:
        numeric_id = int(user_id)
    except ValueError:
        numeric_id = None
    if numeric_id is None or numeric_id not in users_db:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    UserService.delete(numeric_id)
    return

# Эндпоинты для Issue
@issue_router.post("", response_model=IssueResponseTo, status_code=status.HTTP_201_CREATED)
def create_issue(issue_req: IssueRequestTo):
    return IssueService.create(issue_req)

@issue_router.get("", response_model=List[IssueResponseTo])
def get_issues(user_login: Optional[str] = None, title: Optional[str] = None, content: Optional[str] = None):
    if user_login or title or content:
        return IssueService.search(user_login, title, content)
    return IssueService.get_all()

@issue_router.get("/{issue_id}", response_model=IssueResponseTo)
def get_issue(issue_id: int):
    return IssueService.get_by_id(issue_id)

@issue_router.put("", response_model=IssueResponseTo)
def update_issue(issue: IssueResponseTo):
    if not issue.id:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Issue id is required for update")
    return IssueService.update(issue)

@issue_router.delete("/{issue_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_issue(issue_id: str):
    try:
        numeric_id = int(issue_id)
    except ValueError:
        numeric_id = None
    if numeric_id is None or numeric_id not in issues_db:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Issue not found")
    IssueService.delete(numeric_id)
    return

@issue_router.get("/{issue_id}/user", response_model=UserResponseTo)
def get_user_by_issue(issue_id: int):
    issue = IssueService.get_by_id(issue_id)
    return UserService.get_by_id(issue.userId)

@issue_router.get("/{issue_id}/tags", response_model=List[TagResponseTo])
def get_tags_by_issue(issue_id: int):
    return []

@issue_router.get("/{issue_id}/reactions", response_model=List[ReactionResponseTo])
def get_reactions_by_issue(issue_id: int):
    return [reaction for reaction in reactions_db.values() if reaction.issueId == issue_id]

# Эндпоинты для Tag
@tag_router.post("", response_model=TagResponseTo, status_code=status.HTTP_201_CREATED)
def create_tag(tag_req: TagRequestTo):
    return TagService.create(tag_req)

@tag_router.get("", response_model=List[TagResponseTo])
def get_tags():
    return TagService.get_all()

@tag_router.get("/{tag_id}", response_model=TagResponseTo)
def get_tag(tag_id: int):
    return TagService.get_by_id(tag_id)

@tag_router.put("", response_model=TagResponseTo)
def update_tag(tag: TagResponseTo):
    if not tag.id:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Tag id is required for update")
    return TagService.update(tag)

@tag_router.delete("/{tag_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_tag(tag_id: str):
    try:
        numeric_id = int(tag_id)
    except ValueError:
        numeric_id = None
    if numeric_id is None or numeric_id not in tags_db:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tag not found")
    TagService.delete(numeric_id)
    return

# Эндпоинты для Reaction
@reaction_router.post("", response_model=ReactionResponseTo, status_code=status.HTTP_201_CREATED)
def create_reaction(reaction_req: ReactionRequestTo):
    return ReactionService.create(reaction_req)

@reaction_router.get("", response_model=List[ReactionResponseTo])
def get_reactions():
    return ReactionService.get_all()

@reaction_router.get("/{reaction_id}", response_model=ReactionResponseTo)
def get_reaction(reaction_id: int):
    return ReactionService.get_by_id(reaction_id)

@reaction_router.put("", response_model=ReactionResponseTo)
def update_reaction(reaction: ReactionResponseTo):
    if not reaction.id:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Reaction id is required for update")
    return ReactionService.update(reaction)

@reaction_router.delete("/{reaction_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_reaction(reaction_id: str):
    try:
        numeric_id = int(reaction_id)
    except ValueError:
        numeric_id = None
    if numeric_id is None or numeric_id not in reactions_db:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Reaction not found")
    ReactionService.delete(numeric_id)
    return

# Включаем роутеры в приложение с префиксом /api/v1.0
app.include_router(user_router, prefix=api_prefix)
app.include_router(issue_router, prefix=api_prefix)
app.include_router(tag_router, prefix=api_prefix)
app.include_router(reaction_router, prefix=api_prefix)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=24110, log_level="info")
