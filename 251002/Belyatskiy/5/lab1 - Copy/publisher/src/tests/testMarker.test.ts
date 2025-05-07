import { Request, Response } from 'express';
import { MarkController } from '../controllers/mark.controller';
import { MarkService } from '../services/mark.service';
import { MarkRequestTo, MarkResponseTo } from '../dtos/mark.dto';
import {beforeEach, describe, expect, it, jest} from "@jest/globals";

jest.mock('../services/mark.service');

describe('MarkController', () => {
    let markController: MarkController;
    let mockMarkService: jest.Mocked<MarkService>;
    let mockRequest: Partial<Request>;
    let mockResponse: Partial<Response>;
    let responseObject: any;

    beforeEach(() => {
        mockMarkService = new MarkService() as jest.Mocked<MarkService>;
        markController = new MarkController();
        markController['markService'] = mockMarkService;

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
        it('should create a mark and return 201 status', async () => {
            const markDto: MarkRequestTo = {
                name: 'Mark 1',
            };
            const markResponse: MarkResponseTo = {
                id: 1,
                name: 'Mark 1',
            };
            mockRequest = { body: markDto };
            mockMarkService.create.mockReturnValue(markResponse);

            await markController.create(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.create).toHaveBeenCalledWith(markDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(markResponse);
        });
    });

    describe('findAll', () => {
        it('should return all marks and return 200 status', async () => {
            const marks: MarkResponseTo[] = [
                {
                    id: 1,
                    name: 'Mark 1',
                },
                {
                    id: 2,
                    name: 'Mark 2',
                },
            ];
            mockMarkService.findAll.mockReturnValue(marks);

            await markController.findAll(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(marks);
        });
    });

    describe('findById', () => {
        it('should return a mark if found and return 200 status', async () => {
            const mark: MarkResponseTo = {
                id: 1,
                name: 'Mark 1',
            };
            mockRequest = { params: { id: '1' } };
            mockMarkService.findById.mockReturnValue(mark);

            await markController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(mark);
        });

        it('should return 404 status if mark not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkService.findById.mockReturnValue(undefined);

            await markController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkEntity not found' });
        });
    });

    describe('update', () => {
        it('should update a mark and return 200 status', async () => {
            const markDto: MarkRequestTo = {
                name: 'Updated Mark 1',
            };
            const markResponse: MarkResponseTo = {
                id: 1,
                name: 'Updated Mark 1',
            };
            mockRequest = { body: { id: 1, ...markDto } };
            mockMarkService.update.mockReturnValue(markResponse);

            await markController.update(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.update).toHaveBeenCalledWith(1, markDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(markResponse);
        });

        it('should return 404 status if mark not found', async () => {
            const markDto: MarkRequestTo = {
                name: 'Updated Mark 1',
            };
            mockRequest = { body: { id: 1, ...markDto } };
            mockMarkService.update.mockReturnValue(undefined);

            await markController.update(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.update).toHaveBeenCalledWith(1, markDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkEntity not found' });
        });
    });

    describe('delete', () => {
        it('should delete a mark and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkService.delete.mockReturnValue(true);

            await markController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });

        it('should return 404 status if mark not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkService.delete.mockReturnValue(false);

            await markController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockMarkService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkEntity not found' });
        });
    });
});