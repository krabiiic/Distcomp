import {
  HttpException,
  HttpStatus,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { Note } from './note.entity';
import { NoteRequestDto, NoteResponseDto } from '../common/dto/note.dto';
import { DatabaseNoteRepository } from './repositories/note.repository';

@Injectable()
export class NotesService {
  constructor(private readonly noteRepository: DatabaseNoteRepository) {}

  async create(noteDto: NoteRequestDto): Promise<NoteResponseDto> {
    this.validateNote(noteDto); 
    const note = await this.noteRepository.create(noteDto as Note);
    return this.toResponseDto(note);
  }

  async findAll(): Promise<NoteResponseDto[]> {
    return (await this.noteRepository.findAll()).map(this.toResponseDto);
  }

  async findOne(id: number): Promise<NoteResponseDto> {
    // @ts-ignore
    const note = await  this.noteRepository.findOne(parseInt(id));
    if (!note) throw new NotFoundException(`Note with ID ${id} not found`);
    return this.toResponseDto(note);
  }

  async update(noteDto: NoteRequestDto): Promise<NoteResponseDto> {
    // @ts-ignore
    const id = parseInt(noteDto.id);
    this.validateNote(noteDto); 
    // @ts-ignore
    const note = await this.noteRepository.update(noteDto as Note);
    if (!note) throw new NotFoundException(`Note with ID ${id} not found`);
    // @ts-ignore
    if ('articleid' in note) {

      note.articleId = note.articleid;
    }
    return this.toResponseDto(note);
  }

  async delete(id: number): Promise<void> {
    const deleted = await this.noteRepository.delete(id);
    if (!deleted) throw new NotFoundException(`Note with ID ${id} not found`);
  }

  private toResponseDto(note: Note): NoteResponseDto {
    if ('articleid' in note) {
      return {
        id: note.id,
        content: note.content,
        // @ts-ignore
        articleId: note.articleid,
      };
    } else {
      return {
        id: note.id,
        content: note.content,
        articleId: note.articleId,
      };
    }
  }

  private validateNote(noteDto: NoteRequestDto): void {
    if (
      !Number.isSafeInteger(noteDto.articleId) ||
      noteDto.content.length < 2 ||
      noteDto.content.length > 2048
    ) {
      throw new HttpException('Check lengths', HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
