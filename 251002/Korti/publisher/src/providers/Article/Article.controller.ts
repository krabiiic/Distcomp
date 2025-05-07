import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  HttpStatus,
  Param,
  ParseIntPipe,
  Post,
  Put,
  Query,
} from '@nestjs/common';
import { ArticleService } from './Article.service';
import { ArticleResponseTo } from './Dto/ArticleResponseTo';
import { ArticleRequestTo, UpdateArticleTo } from './Dto/ArticleRequestTo';

@Controller('api/v1.0/articles')
export class ArticleController {
  constructor(private readonly articleService: ArticleService) {}

  @Get()
  @HttpCode(HttpStatus.OK)
  async getAll() {
    const articles = await this.articleService.getAllArticles();
    return articles;
  }

  @Post()
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() body: ArticleRequestTo) {
    const article: ArticleResponseTo =
      await this.articleService.createArticle(body);
    return article;
  }

  @Get(':id')
  @HttpCode(HttpStatus.OK)
  async getOne(@Param('id', ParseIntPipe) idArticle: number) {
    const article = await this.articleService.getArticleById(idArticle);
    return article;
  }

  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  async deleteOne(@Param('id', ParseIntPipe) idArticle: number) {
    await this.articleService.deleteArticle(idArticle);
  }

  @Put()
  @HttpCode(HttpStatus.OK)
  async update(@Body() req: UpdateArticleTo) {
    const article = await this.articleService.updateArticle(req);
    return article;
  }

  @Get()
  @HttpCode(HttpStatus.OK)
  async findArticlesByParams(
    @Query('stickerNames') stickerNames?: string[],
    @Query('stickerIds') stickerIds?: number[],
    @Query('editorLogin') editorLogin?: string,
    @Query('title') title?: string,
    @Query('content') content?: string,
  ): Promise<ArticleResponseTo[]> {
    return this.articleService.findByParams({
      stickerNames,
      stickerIds,
      editorLogin,
      title,
      content,
    });
  }
}
