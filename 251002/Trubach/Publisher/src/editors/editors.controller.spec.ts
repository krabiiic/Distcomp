import { Test, TestingModule } from '@nestjs/testing';
import { EditorsController } from './editors.controller';
import { EditorsService } from './editors.service';
import { EditorRequestDto, EditorResponseDto } from '../common/dto/editor.dto';
import { NotFoundException } from '@nestjs/common';

describe('EditorsController', () => {
  let controller: EditorsController;
  let service: EditorsService;

  const mockEditorsService = {
    create: jest.fn((dto: EditorRequestDto) => ({
      id: Date.now(),
      ...dto,
    })),
    findAll: jest.fn(() => [
      { id: 1, name: 'Test Editor', email: 'editor@example.com' },
    ]),
    findOne: jest.fn((id: number) => {
      if (id === 1) {
        return { id: 1, name: 'Test Editor', email: 'editor@example.com' };
      }
      throw new NotFoundException(`Editor with ID ${id} not found`);
    }),
    update: jest.fn((id: number, dto: EditorRequestDto) => {
      if (id === 1) {
        return { id: 1, ...dto };
      }
      throw new NotFoundException(`Editor with ID ${id} not found`);
    }),
    delete: jest.fn((id: number) => {
      if (id === 1) {
        return;
      }
      throw new NotFoundException(`Editor with ID ${id} not found`);
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [EditorsController],
      providers: [
        {
          provide: EditorsService,
          useValue: mockEditorsService,
        },
      ],
    }).compile();

    controller = module.get<EditorsController>(EditorsController);
    service = module.get<EditorsService>(EditorsService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should create an editor', () => {
      const dto: EditorRequestDto = {
        firstname: 'New Editor',
        login: 'neweditor@example.com',
        lastname: 'neweditor@example.com',
        password: 'neweditor@example.com',
      };
      expect(controller.create(dto)).toEqual({
        id: expect.any(Number),
        ...dto,
      });
      expect(service.create).toHaveBeenCalledWith(dto);
    });
  });

  describe('findAll', () => {
    it('should return an array of editors', () => {
      expect(controller.findAll()).toEqual([
        { id: 1, name: 'Test Editor', email: 'editor@example.com' },
      ]);
      expect(service.findAll).toHaveBeenCalled();
    });
  });

  describe('findOne', () => {
    it('should return a single editor', () => {
      expect(controller.findOne(1)).toEqual({
        id: 1,
        name: 'Test Editor',
        email: 'editor@example.com',
      });
      expect(service.findOne).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.findOne(2)).toThrow(NotFoundException);
    });
  });

  describe('update', () => {
   

    it('should throw a NotFoundException for an invalid ID', () => {
      const dto: EditorResponseDto = {
        id: 1,
        firstname: 'New Editor',
        login: 'neweditor@example.com',
        lastname: 'neweditor@example.com',
        password: 'neweditor@example.com',
      };
      expect(() => controller.update(dto)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete an editor', () => {
      expect(controller.delete(1)).toBeUndefined();
      expect(service.delete).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.delete(2)).toThrow(NotFoundException);
    });
  });
});