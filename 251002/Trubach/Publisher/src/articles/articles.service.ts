import {
  HttpException,
  HttpStatus,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { Article } from './article.entity';
import {
  ArticleRequestDto,
  ArticleResponseDto,
} from '../common/dto/article.dto';
import { DatabaseArticleRepository } from './repositories/article.repository';

@Injectable()
export class ArticlesService {
  constructor(private readonly articleRepository: DatabaseArticleRepository) {}

  async create(articleDto: ArticleRequestDto): Promise<ArticleResponseDto> {
    this.validateArticle(articleDto);  
    // @ts-ignore
    const article = await this.articleRepository.create(articleDto as Article);
    return this.toResponseDto(article);
  }

  async findAll(): Promise<ArticleResponseDto[]> {
    return (await this.articleRepository.findAll()).map((v) => this.toResponseDto(v));
  }

  async findOne(id: number): Promise<ArticleResponseDto> {
    const article = await this.articleRepository.findOne(id);
    if (!article)
      throw new NotFoundException(`Article with ID ${id} not found`);
    return this.toResponseDto(article);
  }

  async update(id: number, articleDto: ArticleRequestDto): Promise<ArticleResponseDto> {
    this.validateArticle(articleDto);  
    // @ts-ignore
    const article = await this.articleRepository.update(id, articleDto as Article);
    if (!article)
      throw new NotFoundException(`Article with ID ${id} not found`);
    return this.toResponseDto(article);
  }

  async delete(id: number): Promise<void> {
    // @ts-ignore
    const deleted = await this.articleRepository.delete(parseInt(id));
    if (!deleted)
      throw new NotFoundException(`Article with ID ${id} not found`);
  }

  private toResponseDto(article: Article): ArticleResponseDto {
   
    return {
      id: article.id,
      title: article.title,
      content: article.content,
      // @ts-ignore
      editorId: article.editor_id,
      created: Date.now(),
      modified: Date.now(),
    };
  }

  private validateArticle(articleDto: ArticleRequestDto): void {
    if (
      !Number.isSafeInteger(articleDto.editorId) ||
      articleDto.title.length < 2 ||
      articleDto.title.length > 64 ||
      articleDto.content.length < 4 ||
      articleDto.content.length > 2048 ||
      typeof articleDto.content !== 'string'
    ) {
      throw new HttpException('Check lengths', HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
