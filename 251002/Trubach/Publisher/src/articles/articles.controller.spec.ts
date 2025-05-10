import { Test, TestingModule } from '@nestjs/testing';
import { ArticlesController } from './articles.controller';
import { ArticlesService } from './articles.service';
import { ArticleRequestDto, ArticleResponseDto } from '../common/dto/article.dto';
import { NotFoundException } from '@nestjs/common';

describe('ArticlesController', () => {
  let controller: ArticlesController;
  let service: ArticlesService;

  const mockArticleService = {
    create: jest.fn((dto: ArticleRequestDto) => ({
      id: Date.now(),
      ...dto,
    })),
    findAll: jest.fn(() => [
      { id: 1, title: 'Test Article', content: 'Test content', editorId: 1 },
    ]),
    findOne: jest.fn((id: number) => {
      if (id === 1) {
        return { id: 1, title: 'Test Article', content: 'Test content', editorId: 1 };
      }
      throw new NotFoundException(`Article with ID ${id} not found`);
    }),
    update: jest.fn((id: number, dto: ArticleRequestDto) => {
      if (id === 1) {
        return { id: 1, ...dto };
      }
      throw new NotFoundException(`Article with ID ${id} not found`);
    }),
    delete: jest.fn((id: number) => {
      if (id === 1) {
        return;
      }
      throw new NotFoundException(`Article with ID ${id} not found`);
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ArticlesController],
      providers: [
        {
          provide: ArticlesService,
          useValue: mockArticleService,
        },
      ],
    }).compile();

    controller = module.get<ArticlesController>(ArticlesController);
    service = module.get<ArticlesService>(ArticlesService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should create an article', () => {
      const dto: ArticleRequestDto = {
        title: 'New Article',
        content: 'New content',
        editorId: 1,
      };
      expect(controller.create(dto)).toEqual({
        id: expect.any(Number),
        ...dto,
      });
      expect(service.create).toHaveBeenCalledWith(dto);
    });
  });

  describe('findAll', () => {
    it('should return an array of articles', () => {
      expect(controller.findAll()).toEqual([
        { id: 1, title: 'Test Article', content: 'Test content', editorId: 1 },
      ]);
      expect(service.findAll).toHaveBeenCalled();
    });
  });

  describe('findOne', () => {
    it('should return a single article', () => {
      expect(controller.findOne(1)).toEqual({
        id: 1,
        title: 'Test Article',
        content: 'Test content',
        editorId: 1,
      });
      expect(service.findOne).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.findOne(2)).toThrow(NotFoundException);
    });
  });

  describe('update', () => {
    it('should update an article', () => {
      const dto: ArticleRequestDto = {
        title: 'Updated Article',
        content: 'Updated content',
        editorId: 1,
      };
      expect(controller.update(1, dto)).toEqual({
        id: 1,
        ...dto,
      });
      expect(service.update).toHaveBeenCalledWith(1, dto);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      const dto: ArticleRequestDto = {
        title: 'Updated Article',
        content: 'Updated content',
        editorId: 1,
      };
      expect(() => controller.update(2, dto)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete an article', () => {
      expect(controller.delete(1)).toBeUndefined();
      expect(service.delete).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.delete(2)).toThrow(NotFoundException);
    });
  });
});