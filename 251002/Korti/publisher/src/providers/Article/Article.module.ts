import { Module } from '@nestjs/common';
import { ArticleController } from './Article.controller';
import { ArticleService } from './Article.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Article } from 'src/entities/Article';
import { Editor } from 'src/entities/Editor';

@Module({
  imports: [TypeOrmModule.forFeature([Article, Editor])],
  providers: [ArticleService],
  controllers: [ArticleController],
})
export class ArticleModule {}
