import { Module } from '@nestjs/common';
import { NoteController } from './Note.controller';
import { NoteService } from './Note.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Note } from 'src/entities/Note';
import { Article } from 'src/entities/Article';

@Module({
  imports: [TypeOrmModule.forFeature([Note, Article])],
  providers: [NoteService],
  controllers: [NoteController],
})
export class NoteModule {}
