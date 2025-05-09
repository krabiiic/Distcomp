from fastapi import APIRouter, Depends
from typing import List, Annotated

from src.api.v1.dependencies import get_issues_service
from src.schemas.issues import IssueResponseTo, IssueRequestToUpdate
from src.schemas.issues import IssueRequestToAdd
from src.services.issues import IssuesService

router = APIRouter(prefix="/issues")


@router.get("", response_model=List[IssueResponseTo])
async def get_issues(issues_service: Annotated[IssuesService, Depends(get_issues_service)]):
    return await issues_service.get_issues()


@router.get("/{issue_id}", response_model=IssueResponseTo)
async def get_issue_by_id(issue_id: int,
                            issues_service: Annotated[IssuesService, Depends(get_issues_service)]):
    return await issues_service.get_issue_by_id(issue_id)


@router.post("", response_model=IssueResponseTo, status_code=201)
async def create_issue(issue: IssueRequestToAdd,
                         issues_service: Annotated[IssuesService, Depends(get_issues_service)]):
    issue_response = await issues_service.create_issue(issue)
    return issue_response


@router.delete("/{issue_id}", status_code=204)
async def delete_issue(issue_id: int,
                         issues_service: Annotated[IssuesService, Depends(get_issues_service)]):
    await issues_service.delete_issue(issue_id)
    return issue_id


@router.put("", response_model=IssueResponseTo)
async def update_issue(issue: IssueRequestToUpdate,
                         issues_service: Annotated[IssuesService, Depends(get_issues_service)]):
    return await issues_service.update_issue(issue)