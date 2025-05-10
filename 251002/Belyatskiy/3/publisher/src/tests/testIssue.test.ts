import { Request, Response } from 'express';
import { ArticleController } from '../controllers/article.controller';
import { ArticleService } from '../services/article.service';
import { ArticleRequestTo, ArticleResponseTo } from '../dtos/article.dto';
import {beforeEach, describe, expect, it, jest} from "@jest/globals";

jest.mock('../services/article.service');

describe('ArticleController', () => {
    let articleController: ArticleController;
    let mockArticleService: jest.Mocked<ArticleService>;
    let mockRequest: Partial<Request>;
    let mockResponse: Partial<Response>;
    let responseObject: any;

    beforeEach(() => {
        mockArticleService = new ArticleService() as jest.Mocked<ArticleService>;
        articleController = new ArticleController();
        articleController['articleService'] = mockArticleService;

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
        it('should create an article and return 201 status', async () => {
            const articleDto: ArticleRequestTo = {
                title: 'ArticleEntity Title',
                content: 'ArticleEntity Content',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            const articleResponse: ArticleResponseTo = {
                id: 1,
                title: 'ArticleEntity Title',
                content: 'ArticleEntity Content',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: articleDto };
            mockArticleService.create.mockReturnValue(articleResponse);

            await articleController.create(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.create).toHaveBeenCalledWith(articleDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(articleResponse);
        });
    });

    describe('findAll', () => {
        it('should return all articles and return 200 status', async () => {
            const articles: ArticleResponseTo[] = [
                {
                    id: 1,
                    title: 'ArticleEntity 1',
                    content: 'Content 1',
                    userId: 1,
                    created: Date.now(),
                    modified: Date.now(),
                },
                {
                    id: 2,
                    title: 'ArticleEntity 2',
                    content: 'Content 2',
                    userId: 2,
                    created: Date.now(),
                    modified: Date.now(),
                },
            ];
            mockArticleService.findAll.mockReturnValue(articles);

            await articleController.findAll(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(articles);
        });
    });

    describe('findById', () => {
        it('should return an article if found and return 200 status', async () => {
            const article: ArticleResponseTo = {
                id: 1,
                title: 'ArticleEntity 1',
                content: 'Content 1',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { params: { id: '1' } };
            mockArticleService.findById.mockReturnValue(article);

            await articleController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(article);
        });

        it('should return 404 status if article not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockArticleService.findById.mockReturnValue(undefined);

            await articleController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ArticleEntity not found' });
        });
    });

    describe('update', () => {
        it('should update an article and return 200 status', async () => {
            const articleDto: ArticleRequestTo = {
                title: 'Updated ArticleEntity Title',
                content: 'Updated ArticleEntity Content',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            const articleResponse: ArticleResponseTo = {
                id: 1,
                title: 'Updated ArticleEntity Title',
                content: 'Updated ArticleEntity Content',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: articleDto };
            mockArticleService.update.mockReturnValue(articleResponse);

            await articleController.update(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.update).toHaveBeenCalledWith(1, articleDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(articleResponse);
        });

        it('should return 404 status if article not found', async () => {
            const articleDto: ArticleRequestTo = {
                title: 'Updated ArticleEntity Title',
                content: 'Updated ArticleEntity Content',
                userId: 1,
                created: Date.now(),
                modified: Date.now(),
            };
            mockRequest = { body: articleDto };
            mockArticleService.update.mockReturnValue(undefined);

            await articleController.update(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.update).toHaveBeenCalledWith(1, articleDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ArticleEntity not found' });
        });
    });

    describe('delete', () => {
        it('should delete an article and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockArticleService.delete.mockReturnValue(true);

            await articleController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });

        it('should return 404 status if article not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockArticleService.delete.mockReturnValue(false);

            await articleController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockArticleService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'ArticleEntity not found' });
        });
    });
});