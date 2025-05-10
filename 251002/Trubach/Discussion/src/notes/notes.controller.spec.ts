import { Test, TestingModule } from '@nestjs/testing';
import { NotesController } from './notes.controller';
import { NotesService } from './notes.service';
import { NoteRequestDto, NoteResponseDto } from '../common/dto/note.dto';
import { NotFoundException } from '@nestjs/common';

describe('NotesController', () => {
  let controller: NotesController;
  let service: NotesService;

  const mockNotesService = {
    create: jest.fn((dto: NoteRequestDto) => ({
      id: Date.now(),
      ...dto,
    })),
    findAll: jest.fn(() => [
      { id: 1, title: 'Test Note', content: 'Test content' },
    ]),
    findOne: jest.fn((id: number) => {
      if (id === 1) {
        return { id: 1, title: 'Test Note', content: 'Test content' };
      }
      throw new NotFoundException(`Note with ID ${id} not found`);
    }),
    update: jest.fn((id: number, dto: NoteRequestDto) => {
      if (id === 1) {
        return { id: 1, ...dto };
      }
      throw new NotFoundException(`Note with ID ${id} not found`);
    }),
    delete: jest.fn((id: number) => {
      if (id === 1) {
        return;
      }
      throw new NotFoundException(`Note with ID ${id} not found`);
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [NotesController],
      providers: [
        {
          provide: NotesService,
          useValue: mockNotesService,
        },
      ],
    }).compile();

    controller = module.get<NotesController>(NotesController);
    service = module.get<NotesService>(NotesService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should create a note', () => {
      const dto: NoteRequestDto = {
        articleId: 1,
        content: 'New content',
      };
      expect(controller.create(dto)).toEqual({
        id: expect.any(Number),
        ...dto,
      });
      expect(service.create).toHaveBeenCalledWith(dto);
    });
  });

  describe('findAll', () => {
    it('should return an array of notes', () => {
      expect(controller.findAll()).toEqual([
        { id: 1, title: 'Test Note', content: 'Test content' },
      ]);
      expect(service.findAll).toHaveBeenCalled();
    });
  });

  describe('findOne', () => {
    it('should return a single note', () => {
      expect(controller.findOne(1)).toEqual({
        id: 1,
        title: 'Test Note',
        content: 'Test content',
      });
      expect(service.findOne).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.findOne(2)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete a note', () => {
      expect(controller.delete(1)).toBeUndefined();
      expect(service.delete).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.delete(2)).toThrow(NotFoundException);
    });
  });
});