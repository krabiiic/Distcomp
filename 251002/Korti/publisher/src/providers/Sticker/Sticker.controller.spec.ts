import { Test, TestingModule } from '@nestjs/testing';
import { StickerController } from './Sticker.controller';
import { StickerService } from './Sticker.service';
import { HttpException, HttpStatus } from '@nestjs/common';
import { StickerResponseTo } from './Dto/StickerResponseTo';
import { StickerRequestTo, UpdateStickerTo } from './Dto/StickerRequestTo';

describe('StickerController', () => {
  let controller: StickerController;
  let service: StickerService;

  const mockStickerService = {
    getAllStickers: jest.fn(),
    createSticker: jest.fn(),
    getStickerById: jest.fn(),
    deleteSticker: jest.fn(),
    updateSticker: jest.fn(),
    getStickersByArticleId: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [StickerController],
      providers: [
        {
          provide: StickerService,
          useValue: mockStickerService,
        },
      ],
    }).compile();

    controller = module.get<StickerController>(StickerController);
    service = module.get<StickerService>(StickerService);
  });

  describe('getAll', () => {
    it('should return an array of stickers', async () => {
      const result: StickerResponseTo[] = [{ id: 1, name: 'Test Sticker' }];
      jest.spyOn(service, 'getAllStickers').mockResolvedValue(result);

      const response = await controller.getAll();
      expect(response).toEqual(result);
    });
  });

  describe('create', () => {
    it('should successfully create a sticker', async () => {
      const stickerRequest: StickerRequestTo = {
        name: 'New Sticker',
      };
      const stickerResponse: StickerResponseTo = {
        id: 1,
        name: 'New Sticker',
      };

      jest.spyOn(service, 'createSticker').mockResolvedValue(stickerResponse);

      const response = await controller.create(stickerRequest);
      expect(response).toEqual(stickerResponse);
    });

    it('should throw an error if sticker with the same name already exists', async () => {
      const stickerRequest: StickerRequestTo = {
        name: 'Existing Sticker',
      };
      jest.spyOn(service, 'createSticker').mockImplementation(() => {
        throw new HttpException(
          {
            errorCode: 40005,
            errorMessage: 'Sticker with this name already exist.',
          },
          HttpStatus.BAD_REQUEST,
        );
      });

      await expect(controller.create(stickerRequest)).rejects.toThrow(
        HttpException,
      );
    });
  });

  describe('getOne', () => {
    it('should return a sticker by ID', async () => {
      const stickerResponse: StickerResponseTo = {
        id: 1,
        name: 'Test Sticker',
      };
      jest.spyOn(service, 'getStickerById').mockResolvedValue(stickerResponse);

      const response = await controller.getOne(1);
      expect(response).toEqual(stickerResponse);
    });

    it('should throw an error if sticker does not exist', async () => {
      jest.spyOn(service, 'getStickerById').mockImplementation(() => {
        throw new HttpException(
          { errorCode: 40006, errorMessage: 'Sticker does not exist.' },
          HttpStatus.BAD_REQUEST,
        );
      });

      await expect(controller.getOne(1)).rejects.toThrow(HttpException);
    });
  });

  describe('delete', () => {
    it('should delete a sticker', async () => {
      jest.spyOn(service, 'deleteSticker').mockResolvedValue(undefined);

      await expect(controller.delete(1)).resolves.toBeUndefined();
    });

    it('should throw an error if sticker does not exist', async () => {
      jest.spyOn(service, 'deleteSticker').mockImplementation(() => {
        throw new HttpException(
          { errorCode: 40006, errorMessage: 'Sticker does not exist.' },
          HttpStatus.BAD_REQUEST,
        );
      });

      await expect(controller.delete(1)).rejects.toThrow(HttpException);
    });
  });

  describe('update', () => {
    it('should successfully update a sticker', async () => {
      const stickerRequest: UpdateStickerTo = {
        id: 1,
        name: 'Updated Sticker',
      };
      const stickerResponse: StickerResponseTo = {
        id: 1,
        name: 'Updated Sticker',
      };

      jest.spyOn(service, 'updateSticker').mockResolvedValue(stickerResponse);

      const response = await controller.update(stickerRequest);
      expect(response).toEqual(stickerResponse);
    });

    it('should throw an error if sticker does not exist', async () => {
      const stickerRequest: UpdateStickerTo = {
        id: 1,
        name: 'Non-Existent Sticker',
      };
      jest.spyOn(service, 'updateSticker').mockImplementation(() => {
        throw new HttpException(
          { errorCode: 40006, errorMessage: 'Sticker does not exist.' },
          HttpStatus.BAD_REQUEST,
        );
      });

      await expect(controller.update(stickerRequest)).rejects.toThrow(
        HttpException,
      );
    });
  });

  describe('getByArticles', () => {
    it('should return stickers by article ID', async () => {
      const stickerResponses: StickerResponseTo[] = [
        { id: 1, name: 'Test Sticker' },
        { id: 2, name: 'Another Sticker' },
      ];
      jest
        .spyOn(service, 'getStickersByArticleId')
        .mockResolvedValue(stickerResponses);

      const response = await controller.getByArticles(1);
      expect(response).toEqual(stickerResponses);
    });

    it('should handle errors when fetching stickers by article ID', async () => {
      jest.spyOn(service, 'getStickersByArticleId').mockImplementation(() => {
        throw new HttpException(
          {
            errorCode: 40007,
            errorMessage: 'No stickers found for this article.',
          },
          HttpStatus.NOT_FOUND,
        );
      });

      await expect(controller.getByArticles(1)).rejects.toThrow(HttpException);
    });
  });
});
