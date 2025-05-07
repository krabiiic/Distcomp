import { Module } from '@nestjs/common';
import { APP_FILTER } from '@nestjs/core';
import { DatabaseNoteRepository } from 'src/notes/repositories/note.repository';
import { NotesService } from 'src/notes/notes.service';
import { NotesController } from 'src/notes/notes.controller';
import { HttpExceptionFilter } from './common/exceptions/http-exception.filter';
import { MongooseModule } from '@nestjs/mongoose';
import { NoteSchema } from './notes/schemas/note.schema';
import { HttpModule } from '@nestjs/axios';

@Module({
  imports: [
    MongooseModule.forRoot('mongodb://localhost:9042/distcomp', {
      forceServerObjectId: true,
    }),
    HttpModule,
    MongooseModule.forFeature([
      { name: 'Note', schema: NoteSchema },
    ]),
  ],
  controllers: [
    NotesController,
  ],
  providers: [
    NotesService,
    DatabaseNoteRepository,
    {
      provide: APP_FILTER,
      useClass: HttpExceptionFilter,
    },
  ],
})
export class AppModule {}
