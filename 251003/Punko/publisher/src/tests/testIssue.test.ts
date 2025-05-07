import { Request, Response } from 'express';
import { IssueController } from '../controllers/issue.controller';
import { IssueService } from '../services/issue.service';
import { IssueRequestTo, IssueResponseTo } from '../dtos/issue.dto';
import {beforeEach, describe, expect, it, jest} from "@jest/globals";

jest.mock('../services/issue.service');

describe('IssueController', () => {
    let issueController: IssueController;
    let mockIssueService: jest.Mocked<IssueService>;
    let mockRequest: Partial<Request>;
    let mockResponse: Partial<Response>;
    let responseObject: any;

    beforeEach(() => {
        mockIssueService = new IssueService() as jest.Mocked<IssueService>;
        issueController = new IssueController();
        issueController['issueService'] = mockIssueService;

        mockResponse = {
            status: jest.fn().mockReturnThis(),
            json: jest.fn().mockImplementation((result) => {
                responseObject = result;
                return mockResponse;
            }),
            send: jest.fn().mockReturnThis(),
        };
    });

    describe('create', () => {
        it('should create an issue and return 201 status', async () => {
            const issueDto: IssueRequestTo = {
                title: 'IssueEntity Title',
                content: 'IssueEntity Content',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            const issueResponse: IssueResponseTo = {
                id: 1,
                title: 'IssueEntity Title',
                content: 'IssueEntity Content',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: issueDto };
            mockIssueService.create.mockReturnValue(issueResponse);

            await issueController.create(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.create).toHaveBeenCalledWith(issueDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(issueResponse);
        });
    });

    describe('findAll', () => {
        it('should return all issues and return 200 status', async () => {
            const issues: IssueResponseTo[] = [
                {
                    id: 1,
                    title: 'IssueEntity 1',
                    content: 'Content 1',
                    authorId: 1,
                    created: Date.now(),
                    modified: Date.now(),
                },
                {
                    id: 2,
                    title: 'IssueEntity 2',
                    content: 'Content 2',
                    authorId: 2,
                    created: Date.now(),
                    modified: Date.now(),
                },
            ];
            mockIssueService.findAll.mockReturnValue(issues);

            await issueController.findAll(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(issues);
        });
    });

    describe('findById', () => {
        it('should return an issue if found and return 200 status', async () => {
            const issue: IssueResponseTo = {
                id: 1,
                title: 'IssueEntity 1',
                content: 'Content 1',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { params: { id: '1' } };
            mockIssueService.findById.mockReturnValue(issue);

            await issueController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(issue);
        });

        it('should return 404 status if issue not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockIssueService.findById.mockReturnValue(undefined);

            await issueController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'IssueEntity not found' });
        });
    });

    describe('update', () => {
        it('should update an issue and return 200 status', async () => {
            const issueDto: IssueRequestTo = {
                title: 'Updated IssueEntity Title',
                content: 'Updated IssueEntity Content',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            const issueResponse: IssueResponseTo = {
                id: 1,
                title: 'Updated IssueEntity Title',
                content: 'Updated IssueEntity Content',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: issueDto };
            mockIssueService.update.mockReturnValue(issueResponse);

            await issueController.update(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.update).toHaveBeenCalledWith(1, issueDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(issueResponse);
        });

        it('should return 404 status if issue not found', async () => {
            const issueDto: IssueRequestTo = {
                title: 'Updated IssueEntity Title',
                content: 'Updated IssueEntity Content',
                authorId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: issueDto };
            mockIssueService.update.mockReturnValue(undefined);

            await issueController.update(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.update).toHaveBeenCalledWith(1, issueDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'IssueEntity not found' });
        });
    });

    describe('delete', () => {
        it('should delete an issue and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockIssueService.delete.mockReturnValue(true);

            await issueController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });

        it('should return 404 status if issue not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockIssueService.delete.mockReturnValue(false);

            await issueController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockIssueService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'IssueEntity not found' });
        });
    });
});