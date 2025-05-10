import { MessageController } from '../controllers/message.controller';
import { MessageService } from '../services/message.service';
import { beforeEach, describe, expect, it, jest } from "@jest/globals";
jest.mock('../services/message.service');
describe('MessageController', () => {
    let messageController;
    let mockMessageService;
    let mockRequest;
    let mockResponse;
    let responseObject;
    beforeEach(() => {
        mockMessageService = new MessageService();
        messageController = new MessageController();
        messageController['messageService'] = mockMessageService;
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
        it('should create a message and return 201 status', async () => {
            const messageDto = {
                content: 'MessageEntity Content',
                articleId: 1,
            };
            const messageResponse = {
                id: 1,
                content: 'MessageEntity Content',
                articleId: 1,
            };
            mockRequest = { body: messageDto };
            mockMessageService.create.mockReturnValue(messageResponse);
            await messageController.create(mockRequest, mockResponse);
            expect(mockMessageService.create).toHaveBeenCalledWith(messageDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(messageResponse);
        });
    });
    describe('findAll', () => {
        it('should return all messages and return 200 status', async () => {
            const messages = [
                {
                    id: 1,
                    content: 'MessageEntity 1',
                    articleId: 1,
                },
                {
                    id: 2,
                    content: 'MessageEntity 2',
                    articleId: 2,
                },
            ];
            mockMessageService.findAll.mockReturnValue(messages);
            await messageController.findAll(mockRequest, mockResponse);
            expect(mockMessageService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(messages);
        });
    });
    describe('findById', () => {
        it('should return a message if found and return 200 status', async () => {
            const message = {
                id: 1,
                content: 'MessageEntity 1',
                articleId: 1,
            };
            mockRequest = { params: { id: '1' } };
            mockMessageService.findById.mockReturnValue(message);
            await messageController.findById(mockRequest, mockResponse);
            expect(mockMessageService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(message);
        });
        it('should return 404 status if message not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMessageService.findById.mockReturnValue(undefined);
            await messageController.findById(mockRequest, mockResponse);
            expect(mockMessageService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MessageEntity not found' });
        });
    });
    describe('update', () => {
        it('should update a message and return 200 status', async () => {
            const messageDto = {
                content: 'Updated MessageEntity Content',
                articleId: 1,
            };
            const messageResponse = {
                id: 1,
                content: 'Updated MessageEntity Content',
                articleId: 1,
            };
            mockRequest = { body: { id: 1, ...messageDto } };
            mockMessageService.update.mockReturnValue(messageResponse);
            await messageController.update(mockRequest, mockResponse);
            expect(mockMessageService.update).toHaveBeenCalledWith(1, messageDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(messageResponse);
        });
        it('should return 404 status if message not found', async () => {
            const messageDto = {
                content: 'Updated MessageEntity Content',
                articleId: 1,
            };
            mockRequest = { body: { id: 1, ...messageDto } };
            mockMessageService.update.mockReturnValue(undefined);
            await messageController.update(mockRequest, mockResponse);
            expect(mockMessageService.update).toHaveBeenCalledWith(1, messageDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MessageEntity not found' });
        });
    });
    describe('delete', () => {
        it('should delete a message and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockMessageService.delete.mockReturnValue(true);
            await messageController.delete(mockRequest, mockResponse);
            expect(mockMessageService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });
        it('should return 404 status if message not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMessageService.delete.mockReturnValue(false);
            await messageController.delete(mockRequest, mockResponse);
            expect(mockMessageService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MessageEntity not found' });
        });
    });
});
