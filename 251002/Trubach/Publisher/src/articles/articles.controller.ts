import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  Param,
  Post,
  Put,
} from '@nestjs/common';
import { ArticlesService } from './articles.service';
import {
  ArticleRequestDto,
  ArticleResponseDto,
} from '../common/dto/article.dto';

@Controller('api/v1.0/articles')
export class ArticlesController {
  constructor(private readonly articlesService: ArticlesService) {}

  @Post()
  async create(@Body() articleDto: ArticleRequestDto): Promise<ArticleResponseDto> {
    return await this.articlesService.create(articleDto);
  }

  @Get()
  async findAll(): Promise<ArticleResponseDto[]> {
    return this.articlesService.findAll();
  }

  @Get(':id')
  async findOne(@Param('id') id: string): Promise<ArticleResponseDto> {
    return this.articlesService.findOne(parseInt(id));
  }

  @Put()
  async update(
    @Body() articleDto: ArticleRequestDto,
  ): Promise<ArticleResponseDto> {
    // @ts-ignore
    return await this.articlesService.update(parseInt(articleDto.id), articleDto);
  }

  @HttpCode(204)
  @Delete(':id')
  async delete(@Param('id') id: number): Promise<void> {
    this.articlesService.delete(id);
  }
}
