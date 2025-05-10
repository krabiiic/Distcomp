import { Module } from '@nestjs/common';
import { EditorsController } from 'src/editors/editors.controller';
import { EditorsService } from 'src/editors/editors.service';
import { DatabaseEditorRepository } from 'src/editors/repositories/editor.repository';
import { HttpExceptionFilter } from 'src/common/exceptions/http-exception.filter';
import { APP_FILTER } from '@nestjs/core';
import { ArticlesController } from 'src/articles/articles.controller';
import { DatabaseArticleRepository } from 'src/articles/repositories/article.repository';
import { ArticlesService } from 'src/articles/articles.service';
import { DatabaseLabelRepository } from 'src/labels/repositories/label.repository';
import { LabelsService } from 'src/labels/labels.service';
import { LabelsController } from 'src/labels/labels.controller';
import { HttpModule } from '@nestjs/axios';
import { NotesController } from './notes/notes.controller';

@Module({
  imports: [HttpModule],
  controllers: [
    EditorsController,
    ArticlesController,
    LabelsController,
    NotesController
  ],
  providers: [
    EditorsService,
    DatabaseEditorRepository,
    ArticlesService,
    DatabaseArticleRepository,
    LabelsService,
    DatabaseLabelRepository,
    {
      provide: APP_FILTER,
      useClass: HttpExceptionFilter,
    },
  ],
})
export class AppModule {}
