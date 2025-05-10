import { Request, Response } from 'express';
import { ReactionController } from '../controllers/reaction.controller';
import { ReactionService } from '../services/reaction.service';
import { ReactionRequestTo, ReactionResponseTo } from '../dtos/reaction.dto';
import {beforeEach, describe, expect, it, jest} from "@jest/globals";

jest.mock('../services/reaction.service');

describe('ReactionController', () => {
    let reactionController: ReactionController;
    let mockReactionService: jest.Mocked<ReactionService>;
    let mockRequest: Partial<Request>;
    let mockResponse: Partial<Response>;
    let responseObject: any;

    beforeEach(() => {
        mockReactionService = new ReactionService() as jest.Mocked<ReactionService>;
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
            const reactionDto: ReactionRequestTo = {
                content: 'ReactionEntity Content',
                issueId: 1,
            };
            const reactionResponse: ReactionResponseTo = {
                id: 1,
                content: 'ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: reactionDto };
            mockReactionService.create.mockReturnValue(reactionResponse);

            await reactionController.create(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.create).toHaveBeenCalledWith(reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(reactionResponse);
        });
    });

    describe('findAll', () => {
        it('should return all reactions and return 200 status', async () => {
            const reactions: ReactionResponseTo[] = [
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

            await reactionController.findAll(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reactions);
        });
    });

    describe('findById', () => {
        it('should return a reaction if found and return 200 status', async () => {
            const reaction: ReactionResponseTo = {
                id: 1,
                content: 'ReactionEntity 1',
                issueId: 1,
            };
            mockRequest = { params: { id: '1' } };
            mockReactionService.findById.mockReturnValue(reaction);

            await reactionController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reaction);
        });

        it('should return 404 status if reaction not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.findById.mockReturnValue(undefined);

            await reactionController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });

    describe('update', () => {
        it('should update a reaction and return 200 status', async () => {
            const reactionDto: ReactionRequestTo = {
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            const reactionResponse: ReactionResponseTo = {
                id: 1,
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: { id: 1, ...reactionDto } };
            mockReactionService.update.mockReturnValue(reactionResponse);

            await reactionController.update(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.update).toHaveBeenCalledWith(1, reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(reactionResponse);
        });

        it('should return 404 status if reaction not found', async () => {
            const reactionDto: ReactionRequestTo = {
                content: 'Updated ReactionEntity Content',
                issueId: 1,
            };
            mockRequest = { body: { id: 1, ...reactionDto } };
            mockReactionService.update.mockReturnValue(undefined);

            await reactionController.update(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.update).toHaveBeenCalledWith(1, reactionDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });

    describe('delete', () => {
        it('should delete a reaction and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.delete.mockReturnValue(true);

            await reactionController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });

        it('should return 404 status if reaction not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockReactionService.delete.mockReturnValue(false);

            await reactionController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockReactionService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ReactionEntity not found' });
        });
    });
});