import { Module } from '@nestjs/common';
import { EditorService } from './Editor.Service';
import { EditorController } from './Editor.Controller';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Editor } from 'src/entities/Editor';
import { Article } from 'src/entities/Article';

@Module({
  imports: [TypeOrmModule.forFeature([Editor, Article])],
  controllers: [EditorController],
  providers: [EditorService],
})
export class EditorModule {}
