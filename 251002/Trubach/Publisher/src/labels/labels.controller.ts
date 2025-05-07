import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  Param,
  Post,
  Put,
} from '@nestjs/common';
import { LabelsService } from './labels.service';
import { LabelRequestDto, LabelResponseDto } from '../common/dto/label.dto';

@Controller('api/v1.0/labels')
export class LabelsController {
  constructor(private readonly labelsService: LabelsService) {}

  @Post()
  async create(@Body() labelDto: LabelRequestDto): Promise<LabelResponseDto> {
    return await  this.labelsService.create(labelDto);
  }

  @Get()
  async findAll(): Promise<LabelResponseDto[]> {
    return await this.labelsService.findAll();
  }

  @Get(':id')
  async findOne(@Param('id') id: number): Promise<LabelResponseDto> {
    return await this.labelsService.findOne(id);
  }

  @Put()
  async update(
    @Body() labelDto: LabelRequestDto,
  ): Promise<LabelResponseDto> {
    return await this.labelsService.update(labelDto);
  }

  @HttpCode(204)
  @Delete(':id')
  async delete(@Param('id') id: number): Promise<void> {
    await this.labelsService.delete(id);
  }
}
