import { Test, TestingModule } from '@nestjs/testing';
import { LabelsController } from './labels.controller';
import { LabelsService } from './labels.service';
import { LabelRequestDto, LabelResponseDto } from '../common/dto/label.dto';
import { NotFoundException } from '@nestjs/common';

describe('LabelsController', () => {
  let controller: LabelsController;
  let service: LabelsService;

  const mockLabelsService = {
    create: jest.fn((dto: LabelRequestDto) => ({
      id: Date.now(),
      ...dto,
    })),
    findAll: jest.fn(() => [
      { id: 1, name: 'Test Label' },
    ]),
    findOne: jest.fn((id: number) => {
      if (id === 1) {
        return { id: 1, name: 'Test Label' };
      }
      throw new NotFoundException(`Label with ID ${id} not found`);
    }),
    update: jest.fn((id: number, dto: LabelRequestDto) => {
      if (id === 1) {
        return { id: 1, ...dto };
      }
      throw new NotFoundException(`Label with ID ${id} not found`);
    }),
    delete: jest.fn((id: number) => {
      if (id === 1) {
        return;
      }
      throw new NotFoundException(`Label with ID ${id} not found`);
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [LabelsController],
      providers: [
        {
          provide: LabelsService,
          useValue: mockLabelsService,
        },
      ],
    }).compile();

    controller = module.get<LabelsController>(LabelsController);
    service = module.get<LabelsService>(LabelsService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should create a label', () => {
      const dto: LabelRequestDto = {
        name: 'New Label',
      };
      expect(controller.create(dto)).toEqual({
        id: expect.any(Number),
        ...dto,
      });
      expect(service.create).toHaveBeenCalledWith(dto);
    });
  });

  describe('findAll', () => {
    it('should return an array of labels', () => {
      expect(controller.findAll()).toEqual([
        { id: 1, name: 'Test Label' },
      ]);
      expect(service.findAll).toHaveBeenCalled();
    });
  });

  describe('findOne', () => {
    it('should return a single label', () => {
      expect(controller.findOne(1)).toEqual({
        id: 1,
        name: 'Test Label',
      });
      expect(service.findOne).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.findOne(2)).toThrow(NotFoundException);
    });
  });


  describe('delete', () => {
    it('should delete a label', () => {
      expect(controller.delete(1)).toBeUndefined();
      expect(service.delete).toHaveBeenCalledWith(1);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => controller.delete(2)).toThrow(NotFoundException);
    });
  });
});