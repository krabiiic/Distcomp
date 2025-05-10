import { Test, TestingModule } from '@nestjs/testing';
import { NotesService } from './notes.service';
import { Note } from './note.entity';
import { NotFoundException } from '@nestjs/common';
import { DatabaseNoteRepository } from './repositories/note.repository';
import { NoteRequestDto, NoteResponseDto } from '../common/dto/note.dto';

describe('NotesService', () => {
  let service: NotesService;
  let repository: DatabaseNoteRepository;

  beforeEach(async () => {
    repository = new DatabaseNoteRepository();
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        NotesService,
        {
          provide: DatabaseNoteRepository,
          useValue: repository,
        },
      ],
    }).compile();

    service = module.get<NotesService>(NotesService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('create', () => {
    it('should create a note', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'New content',
      };
      const result = service.create(dto);
      expect(result).toEqual({
        id: expect.any(Number),
        ...dto,
      });
    });
  });

  describe('findAll', () => {
    it('should return an array of notes', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'Test content',
      };
      const res = service.create(dto);
      const notes = service.findAll();
      expect(notes).toEqual([
        res
      ]);
    });
  });

  describe('findOne', () => {
    it('should return a single note', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'Test content',
      };
      const createdNote = service.create(dto);
      const note = service.findOne(createdNote.id);
      expect(note).toEqual(createdNote);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.findOne(999)).toThrow(NotFoundException);
    });
  });

  describe('update', () => {
    it('should update a note', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'Test content',
      };
      const createdNote = service.create(dto);
      const updatedDto: NoteResponseDto = {
        id: createdNote.id,
        articleId: 3,
        content: 'Updated content',
      };
      const updatedNote = service.update(updatedDto);
      expect(updatedNote).toEqual({
        ...updatedDto,
      });
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      const updatedDto: NoteResponseDto = {
        id: 1243124,
        articleId: 1,
        content: 'Updated content',
      };
      expect(() => service.update(updatedDto)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete a note', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'Test content',
      };
      const createdNote = service.create(dto);
      service.delete(createdNote.id);
      expect(() => service.findOne(createdNote.id)).toThrow(NotFoundException);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.delete(999)).toThrow(NotFoundException);
    });
  });
});
