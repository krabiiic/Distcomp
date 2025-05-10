import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { Label } from './label.entity';
import { LabelRequestDto, LabelResponseDto } from '../common/dto/label.dto';
import { DatabaseLabelRepository } from './repositories/label.repository';

@Injectable()
export class LabelsService {
  constructor(private readonly labelRepository: DatabaseLabelRepository) {}

  async create(labelDto: LabelRequestDto): Promise<LabelResponseDto> {
    this.validateLabel(labelDto);  
    const label = await this.labelRepository.create(labelDto as Label);
    return this.toResponseDto(label);
  }

  async findAll(): Promise<LabelResponseDto[]> {
    return (await this.labelRepository.findAll()).map(this.toResponseDto);
  }

  async findOne(id: number): Promise<LabelResponseDto> {
    const label = await this.labelRepository.findOne(id);
    if (!label) throw new NotFoundException(`Label with ID ${id} not found`);
    return this.toResponseDto(label);
  }

  async update(labelDto: LabelRequestDto): Promise<LabelResponseDto> {
    this.validateLabel(labelDto);  
    const label = await this.labelRepository.update(labelDto as Label);
    // @ts-ignore
    if (!label) throw new NotFoundException(`Label with ID ${labelDto.id} not found`);
    return this.toResponseDto(label);
  }

  async delete(id: number): Promise<void> {
    // @ts-ignore
    const deleted = await this.labelRepository.delete(parseInt(id));
    if (!deleted) throw new NotFoundException(`Label with ID ${id} not found`);
  }

  private toResponseDto(label: Label): LabelResponseDto {
    return {
      id: label.id,
      name: label.name,
    };
  }

  private validateLabel(labelDto: LabelRequestDto): void {
    if (labelDto.name.length < 2 || labelDto.name.length > 32) {
      throw new HttpException('Name is wrong', HttpStatus.FORBIDDEN); 
      
    }
  }
}
