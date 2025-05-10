import { Request, Response } from 'express';
import { UserController } from '../controllers/user.controller';
import { UserService } from '../services/user.sevice';
import { UserRequestTo, UserResponseTo } from '../dtos/user.dto';
import {beforeEach, describe, expect, it, jest} from "@jest/globals";

jest.mock('../services/user.sevice');

describe('UserController', () => {
    let userController: UserController;
    let mockUserService: jest.Mocked<UserService>;
    let mockRequest: Partial<Request>;
    let mockResponse: Partial<Response>;
    let responseObject: any;

    beforeEach(() => {
        mockUserService = new UserService() as jest.Mocked<UserService>;
        userController = new UserController();
        userController['userService'] = mockUserService;

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
        it('should create an user and return 201 status', async () => {
            const userDto: UserRequestTo = {
                login: 'johndoe',
                password: 'password123',
                firstname: 'John',
                lastname: 'Doe',
            };
            const userResponse: UserResponseTo = {
                id: 1,
                login: 'johndoe',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: userDto };
            mockUserService.create.mockReturnValue(userResponse);

            await userController.create(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.create).toHaveBeenCalledWith(userDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(userResponse);
        });
    });

    describe('findAll', () => {
        it('should return all users and return 200 status', async () => {
            const users: UserResponseTo[] = [
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
            mockUserService.findAll.mockReturnValue(users);

            await userController.findAll(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(users);
        });
    });

    describe('findById', () => {
        it('should return an user if found and return 200 status', async () => {
            const user: UserResponseTo = {
                id: 1,
                login: 'johndoe',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { params: { id: '1' } };
            mockUserService.findById.mockReturnValue(user);

            await userController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(user);
        });

        it('should return 404 status if user not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockUserService.findById.mockReturnValue(undefined);

            await userController.findById(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'UserEntity not found' });
        });
    });

    describe('update', () => {
        it('should update an user and return 200 status', async () => {
            const userDto: UserRequestTo = {
                login: 'johndoe_updated',
                password: 'newpassword123',
                firstname: 'John',
                lastname: 'Doe',
            };
            const userResponse: UserResponseTo = {
                id: 1,
                login: 'johndoe_updated',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: userDto };
            mockUserService.update.mockReturnValue(userResponse);

            await userController.update(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.update).toHaveBeenCalledWith(1, userDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(userResponse);
        });

        it('should return 404 status if user not found', async () => {
            const userDto: UserRequestTo = {
                login: 'johndoe_updated',
                password: 'newpassword123',
                firstname: 'John',
                lastname: 'Doe',
            };
            mockRequest = { body: userDto };
            mockUserService.update.mockReturnValue(undefined);

            await userController.update(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.update).toHaveBeenCalledWith(1, userDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'UserEntity not found' });
        });
    });

    describe('delete', () => {
        it('should delete an user and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockUserService.delete.mockReturnValue(true);

            await userController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });

        it('should return 404 status if user not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockUserService.delete.mockReturnValue(false);

            await userController.delete(mockRequest as Request, mockResponse as Response);

            expect(mockUserService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'UserEntity not found' });
        });
    });
});