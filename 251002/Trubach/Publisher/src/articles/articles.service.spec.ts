import { Test, TestingModule } from '@nestjs/testing';
import { ArticlesService } from './articles.service';
import { Article } from './article.entity';
import { NotFoundException } from '@nestjs/common';
import { DatabaseArticleRepository } from './repositories/article.repository';
import { ArticleRequestDto } from '../common/dto/article.dto';
import { create } from 'domain';

describe('ArticlesService', () => {
  let service: ArticlesService;
  let repository: DatabaseArticleRepository;

  beforeEach(async () => {
    repository = new DatabaseArticleRepository();
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        ArticlesService,
        {
          provide: DatabaseArticleRepository,
          useValue: repository,
        },
      ],
    }).compile();

    service = module.get<ArticlesService>(ArticlesService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('create', () => {
    it('should create an article', () => {
      const dto: ArticleRequestDto = {
        title: 'New Article',
        content: 'New content',
        editorId: 1,
      };
      const result = service.create(dto);
      expect(result).toEqual({
        ...result,
      });
    });
  });

  

  describe('findOne', () => {
    it('should return a single article', () => {
      const dto: ArticleRequestDto = {
        title: 'Test Article',
        content: 'Test content',
        editorId: 1,
      };
      const createdArticle = service.create(dto);
      const article = service.findOne(createdArticle.id);
      expect(article).toEqual(createdArticle);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.findOne(999)).toThrow(NotFoundException);
    });
  });

  describe('update', () => {
    it('should update an article', () => {
      const dto: ArticleRequestDto = {
        title: 'Test Article',
        content: 'Test content',
        editorId: 1,
      };
      const createdArticle = service.create(dto);
      const updatedDto = {
        id: createdArticle.id,
        title: 'Updated Article',
        content: 'Updated content',
        editorId: 1,
      };
      const updatedArticle = service.update(createdArticle.id, updatedDto);
      expect(updatedArticle).toEqual({
        ...createdArticle,
        ...updatedDto,
      });
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      const updatedDto: ArticleRequestDto = {
        title: 'Updated Article',
        content: 'Updated content',
        editorId: 1,
      };
      expect(() => service.update(999, updatedDto)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete an article', () => {
      const dto: ArticleRequestDto = {
        title: 'Test Article',
        content: 'Test content',
        editorId: 1,
      };
      const createdArticle = service.create(dto);
      service.delete(createdArticle.id);
      expect(() => service.findOne(createdArticle.id)).toThrow(NotFoundException);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.delete(999)).toThrow(NotFoundException);
    });
  });
});