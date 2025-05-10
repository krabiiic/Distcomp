import { ReactionController } from '../controllers/reaction.controller';
import { ReactionService } from '../services/reaction.service';
import { beforeEach, describe, expect, it, jest } from "@jest/globals";
jest.mock('../services/reaction.service');
describe('ReactionController', () => {
    let reactionController;
    let mockReactionService;
    let mockRequest;
    let mockResponse;
    let responseObject;
    beforeEach(() => {
        mockReactionService = new ReactionService();
        reactionController = new ReactionController();
        reactionController['reactionService'] = mockReactionService;
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
        it('should create a reaction and return 201 status', async () => {
            const reactionDto = {
                content: 'ReactionEntity Content',
                issueId: 1,
            };
            const reactionResponse = {
                id: 1,
                content: 'ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: reactionDto };
            mockReactionService.create.mockReturnValue(reactionResponse);
            await reactionController.create(mockRequest, mockResponse);
            expect(mockReactionService.create).toHaveBeenCalledWith(reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(reactionResponse);
        });
    });
    describe('findAll', () => {
        it('should return all reactions and return 200 status', async () => {
            const reactions = [
                {
                    id: 1,
                    content: 'ReactionEntity 1',
                    issueId: 1,
                },
                {
                    id: 2,
                    content: 'ReactionEntity 2',
                    issueId: 2,
                },
            ];
            mockReactionService.findAll.mockReturnValue(reactions);
            await reactionController.findAll(mockRequest, mockResponse);
            expect(mockReactionService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reactions);
        });
    });
    describe('findById', () => {
        it('should return a reaction if found and return 200 status', async () => {
            const reaction = {
                id: 1,
                content: 'ReactionEntity 1',
                issueId: 1,
            };
            mockRequest = { params: { id: '1' } };
            mockReactionService.findById.mockReturnValue(reaction);
            await reactionController.findById(mockRequest, mockResponse);
            expect(mockReactionService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reaction);
        });
        it('should return 404 status if reaction not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.findById.mockReturnValue(undefined);
            await reactionController.findById(mockRequest, mockResponse);
            expect(mockReactionService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });
    describe('update', () => {
        it('should update a reaction and return 200 status', async () => {
            const reactionDto = {
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            const reactionResponse = {
                id: 1,
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: { id: 1, ...reactionDto } };
            mockReactionService.update.mockReturnValue(reactionResponse);
            await reactionController.update(mockRequest, mockResponse);
            expect(mockReactionService.update).toHaveBeenCalledWith(1, reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reactionResponse);
        });
        it('should return 404 status if reaction not found', async () => {
            const reactionDto = {
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: { id: 1, ...reactionDto } };
            mockReactionService.update.mockReturnValue(undefined);
            await reactionController.update(mockRequest, mockResponse);
            expect(mockReactionService.update).toHaveBeenCalledWith(1, reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });
    describe('delete', () => {
        it('should delete a reaction and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.delete.mockReturnValue(true);
            await reactionController.delete(mockRequest, mockResponse);
            expect(mockReactionService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });
        it('should return 404 status if reaction not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.delete.mockReturnValue(false);
            await reactionController.delete(mockRequest, mockResponse);
            expect(mockReactionService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });
});
