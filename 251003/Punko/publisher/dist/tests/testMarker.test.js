import { MarkerController } from '../controllers/marker.controller';
import { MarkerService } from '../services/marker.service';
import { beforeEach, describe, expect, it, jest } from "@jest/globals";
jest.mock('../services/marker.service');
describe('MarkerController', () => {
    let markerController;
    let mockMarkerService;
    let mockRequest;
    let mockResponse;
    let responseObject;
    beforeEach(() => {
        mockMarkerService = new MarkerService();
        markerController = new MarkerController();
        markerController['markerService'] = mockMarkerService;
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
        it('should create a marker and return 201 status', async () => {
            const markerDto = {
                name: 'Marker 1',
            };
            const markerResponse = {
                id: 1,
                name: 'Marker 1',
            };
            mockRequest = { body: markerDto };
            mockMarkerService.create.mockReturnValue(markerResponse);
            await markerController.create(mockRequest, mockResponse);
            expect(mockMarkerService.create).toHaveBeenCalledWith(markerDto);
            expect(mockResponse.status).toHaveBeenCalledWith(201);
            expect(responseObject).toEqual(markerResponse);
        });
    });
    describe('findAll', () => {
        it('should return all markers and return 200 status', async () => {
            const markers = [
                {
                    id: 1,
                    name: 'Marker 1',
                },
                {
                    id: 2,
                    name: 'Marker 2',
                },
            ];
            mockMarkerService.findAll.mockReturnValue(markers);
            await markerController.findAll(mockRequest, mockResponse);
            expect(mockMarkerService.findAll).toHaveBeenCalled();
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(markers);
        });
    });
    describe('findById', () => {
        it('should return a marker if found and return 200 status', async () => {
            const marker = {
                id: 1,
                name: 'Marker 1',
            };
            mockRequest = { params: { id: '1' } };
            mockMarkerService.findById.mockReturnValue(marker);
            await markerController.findById(mockRequest, mockResponse);
            expect(mockMarkerService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(marker);
        });
        it('should return 404 status if marker not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkerService.findById.mockReturnValue(undefined);
            await markerController.findById(mockRequest, mockResponse);
            expect(mockMarkerService.findById).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkerEntity not found' });
        });
    });
    describe('update', () => {
        it('should update a marker and return 200 status', async () => {
            const markerDto = {
                name: 'Updated Marker 1',
            };
            const markerResponse = {
                id: 1,
                name: 'Updated Marker 1',
            };
            mockRequest = { body: { id: 1, ...markerDto } };
            mockMarkerService.update.mockReturnValue(markerResponse);
            await markerController.update(mockRequest, mockResponse);
            expect(mockMarkerService.update).toHaveBeenCalledWith(1, markerDto);
            expect(mockResponse.status).toHaveBeenCalledWith(200);
            expect(responseObject).toEqual(markerResponse);
        });
        it('should return 404 status if marker not found', async () => {
            const markerDto = {
                name: 'Updated Marker 1',
            };
            mockRequest = { body: { id: 1, ...markerDto } };
            mockMarkerService.update.mockReturnValue(undefined);
            await markerController.update(mockRequest, mockResponse);
            expect(mockMarkerService.update).toHaveBeenCalledWith(1, markerDto);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkerEntity not found' });
        });
    });
    describe('delete', () => {
        it('should delete a marker and return 204 status', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkerService.delete.mockReturnValue(true);
            await markerController.delete(mockRequest, mockResponse);
            expect(mockMarkerService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(204);
            expect(mockResponse.send).toHaveBeenCalled();
        });
        it('should return 404 status if marker not found', async () => {
            mockRequest = { params: { id: '1' } };
            mockMarkerService.delete.mockReturnValue(false);
            await markerController.delete(mockRequest, mockResponse);
            expect(mockMarkerService.delete).toHaveBeenCalledWith(1);
            expect(mockResponse.status).toHaveBeenCalledWith(404);
            expect(responseObject).toEqual({ message: 'MarkerEntity not found' });
        });
    });
});
