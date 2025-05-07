import { AuthorController } from '../controllers/author.controller';
import { AuthorService } from '../services/author.sevice';
import { beforeEach, describe, expect, it, jest } from "@jest/globals";
jest.mock('../services/author.sevice');
describe('AuthorController', () => {
    let authorController;
    let mockAuthorService;
    let mockRequest;
    let mockResponse;
    let responseObject;
    beforeEach(() => {
        mockAuthorService = new AuthorService();
        authorController = new AuthorController();
        authorController['authorService'] = mockAuthorService;
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
        it('should create an author and return 201 status', async () => {
            const authorDto = {
                login: 'johndoe',
                password: 'password123',
                firstname: 'John',
                lastname: 'Doe',
            };
            const authorResponse = {
                id: 1,
                login: 'johndoe',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: authorDto };
            mockAuthorService.create.mockReturnValue(authorResponse);
            await authorController.create(mockRequest, mockResponse);
            expect(mockAuthorService.create).toHaveBeenCalledWith(authorDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(authorResponse);
        });
    });
    describe('findAll', () => {
        it('should return all authors and return 200 status', async () => {
            const authors = [
                {
                    id: 1,
                    login: 'johndoe',
                    firstname: 'John',
                    lastname: 'Doe',
                },
                {
                    id: 2,
                    login: 'janedoe',
                    firstname: 'Jane',
                    lastname: 'Doe',
                },
            ];
            mockAuthorService.findAll.mockReturnValue(authors);
            await authorController.findAll(mockRequest, mockResponse);
            expect(mockAuthorService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(authors);
        });
    });
    describe('findById', () => {
        it('should return an author if found and return 200 status', async () => {
            const author = {
                id: 1,
                login: 'johndoe',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { params: { id: '1' } };
            mockAuthorService.findById.mockReturnValue(author);
            await authorController.findById(mockRequest, mockResponse);
            expect(mockAuthorService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(author);
        });
        it('should return 404 status if author not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockAuthorService.findById.mockReturnValue(undefined);
            await authorController.findById(mockRequest, mockResponse);
            expect(mockAuthorService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'AuthorEntity not found' });
        });
    });
    describe('update', () => {
        it('should update an author and return 200 status', async () => {
            const authorDto = {
                login: 'johndoe_updated',
                password: 'newpassword123',
                firstname: 'John',
                lastname: 'Doe',
            };
            const authorResponse = {
                id: 1,
                login: 'johndoe_updated',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: authorDto };
            mockAuthorService.update.mockReturnValue(authorResponse);
            await authorController.update(mockRequest, mockResponse);
            expect(mockAuthorService.update).toHaveBeenCalledWith(1, authorDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(authorResponse);
        });
        it('should return 404 status if author not found', async () => {
            const authorDto = {
                login: 'johndoe_updated',
                password: 'newpassword123',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: authorDto };
            mockAuthorService.update.mockReturnValue(undefined);
            await authorController.update(mockRequest, mockResponse);
            expect(mockAuthorService.update).toHaveBeenCalledWith(1, authorDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'AuthorEntity not found' });
        });
    });
    describe('delete', () => {
        it('should delete an author and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockAuthorService.delete.mockReturnValue(true);
            await authorController.delete(mockRequest, mockResponse);
            expect(mockAuthorService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });
        it('should return 404 status if author not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockAuthorService.delete.mockReturnValue(false);
            await authorController.delete(mockRequest, mockResponse);
            expect(mockAuthorService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'AuthorEntity not found' });
        });
    });
});
