import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Note } from '../note.entity';
import { INoteRepository } from '../interfaces/note.interface';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';

@Injectable()
export class DatabaseNoteRepository implements INoteRepository {
  constructor(
    @InjectModel(Note.name) private noteModel: Model<Note>,
    private readonly httpService: HttpService
  ) {}

  counter: number = 0;

  private async checkArticleExists(articleId: number): Promise<boolean> {
    try {
      const response = await firstValueFrom(
        this.httpService.get(`http://localhost:24110/api/v1.0/articles/${articleId}`)
      );
      return response.status === 200;
    } catch (error) {
      console.log("Error checking article:", error);
      return false;
    }
  }

  private transformNote(note: Note): any {
    return {
      // @ts-ignore
      id: parseInt(note._id as any),
      content: note.content,
      articleId: note.articleId,
    };
  }

  async create(note: Note): Promise<any> {
    const articleExists = await this.checkArticleExists(note.articleId);
    if (!articleExists) {
      throw new HttpException('Article not found', HttpStatus.FORBIDDEN);
    }

    const existingNote = await this.noteModel.findOne({ content: note.content }).exec();
    if (existingNote) {
      throw new HttpException('Note with this content already exists', HttpStatus.FORBIDDEN);
    }

    const createdNote = new this.noteModel({ ...note, _id: Math.floor(Math.random() * 1000000).toString() });
    const savedNote = await createdNote.save();
    
    return this.transformNote(savedNote);
  }

  async findAll(): Promise<any[]> {
    const notes = await this.noteModel.find().exec();
    return notes.map(this.transformNote);
  }

  async findOne(id: number): Promise<any | null> {
    const note = await this.noteModel.findById(id).exec();
    return note ? this.transformNote(note) : null;
  }

  async update(note: Note): Promise<any | null> {
    const updatedNote = await this.noteModel
      .findByIdAndUpdate(
        note.id,
        { content: note.content, articleId: note.articleId },
        { new: true }
      )
      .exec();
    return updatedNote ? this.transformNote(updatedNote) : null;
  }

  async delete(id: number): Promise<boolean> {
    const result = await this.noteModel.findByIdAndDelete(parseInt(id as any)).exec();
    return result !== null;
  }
}