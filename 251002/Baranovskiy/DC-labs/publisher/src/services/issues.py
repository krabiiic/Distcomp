from fastapi import HTTPException

from src.models.models import Issue
from src.schemas.issues import IssueRequestToAdd, IssueResponseTo, IssueRequestToUpdate
from src.utils.repository import AbstractRepository, NotFoundError


class IssuesService:
    def __init__(self, issues_repo: AbstractRepository):
        self.issues_repo: AbstractRepository = issues_repo

    async def create_issue(self, issue: IssueRequestToAdd) -> IssueResponseTo:
        issue_model = Issue(
            title=issue.title,
            content=issue.content,
            user_id=issue.user_id,
        )

        try:
            created_model = await self.issues_repo.create(issue_model)
            return IssueResponseTo.model_validate(created_model)
        except ValueError as e:
            raise HTTPException(status_code=403, detail=str(e))

    async def get_issues(self) -> list[IssueResponseTo]:
        issues = await self.issues_repo.get_all()
        return [IssueResponseTo.model_validate(issue) for issue in issues]

    async def get_issue_by_id(self, issue_id: int) -> IssueResponseTo:
        try:
            issue = await self.issues_repo.get_by_id(issue_id)
            return IssueResponseTo.model_validate(issue)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Issue not found")

    async def delete_issue(self, issue_id: int):
        try:
            await self.issues_repo.delete(issue_id)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Issue not found")

    async def update_issue(self, issue: IssueRequestToUpdate) -> IssueResponseTo:
        try:
            existing_issue = await self.issues_repo.get_by_id(issue.id)

            existing_issue.title = issue.title
            existing_issue.content = issue.content
            existing_issue.user_id = issue.user_id

            updated_issue = await self.issues_repo.update(existing_issue)
            return IssueResponseTo.model_validate(updated_issue)
        except NotFoundError:
            raise HTTPException(status_code=404, detail="Issue not found")