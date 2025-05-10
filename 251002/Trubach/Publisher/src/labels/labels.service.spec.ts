import { Test, TestingModule } from '@nestjs/testing';
import { LabelsService } from './labels.service';
import { Label } from './label.entity';
import { NotFoundException } from '@nestjs/common';
import { DatabaseLabelRepository } from './repositories/label.repository';
import { LabelRequestDto, LabelResponseDto } from '../common/dto/label.dto';

describe('LabelsService', () => {
  let service: LabelsService;
  let repository: DatabaseLabelRepository;

  beforeEach(async () => {
    repository = new DatabaseLabelRepository();
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        LabelsService,
        {
          provide: DatabaseLabelRepository,
          useValue: repository,
        },
      ],
    }).compile();

    service = module.get<LabelsService>(LabelsService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('create', () => {
    it('should create a label', () => {
      const dto: LabelRequestDto = {
        name: 'New Label',
      };
      const result = service.create(dto);
      expect(result).toEqual({
        id: expect.any(Number),
        ...dto,
      });
    });
  });

  describe('findAll', () => {
    it('should return an array of labels', () => {
      const dto: LabelRequestDto = {
        name: 'Test Label',
      };
      service.create(dto);
      const labels = service.findAll();
      expect(labels).toEqual([
        {
          id: expect.any(Number),
          name: 'Test Label',
        },
      ]);
    });
  });

  describe('findOne', () => {
    it('should return a single label', () => {
      const dto: LabelRequestDto = {
        name: 'Test Label',
      };
      const createdLabel = service.create(dto);
      const label = service.findOne(createdLabel.id);
      expect(label).toEqual(createdLabel);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.findOne(999)).toThrow(NotFoundException);
    });
  });

  describe('update', () => {
    it('should update a label', () => {
      const dto: LabelRequestDto = {
        name: 'Test Label',
      };
      const createdLabel = service.create(dto);
      const updatedDto = {
        id: createdLabel.id,
        name: 'Updated Label',
      };
      const updatedLabel = service.update( updatedDto);
      expect(updatedLabel).toEqual({
        ...updatedDto,
      });
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      const updatedDto: LabelResponseDto = {
        id: 234234,
        name: 'Updated Label',
      };
      expect(() => service.update(updatedDto)).toThrow(NotFoundException);
    });
  });

  describe('delete', () => {
    it('should delete a label', () => {
      const dto: LabelRequestDto = {
        name: 'Test Label',
      };
      const createdLabel = service.create(dto);
      service.delete(createdLabel.id);
      expect(() => service.findOne(createdLabel.id)).toThrow(NotFoundException);
    });

    it('should throw a NotFoundException for an invalid ID', () => {
      expect(() => service.delete(999)).toThrow(NotFoundException);
    });
  });
});