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
import { EditorsService } from './editors.service';
import { EditorRequestDto, EditorResponseDto } from '../common/dto/editor.dto';

@Controller('api/v1.0/editors')
export class EditorsController {
  constructor(private readonly editorsService: EditorsService) {}

  @Post()
  async create(@Body() editorDto: EditorRequestDto): Promise<EditorResponseDto> {
    let a = this.editorsService.create(editorDto);
    return a;
  }

  @Get()
  async findAll(): Promise<EditorResponseDto[]> {
    return this.editorsService.findAll();
  }

  @Get(':id')
  async findOne(@Param('id') id: number): Promise<EditorResponseDto> {
    return this.editorsService.findOne(id);
  }

  @Put()
  async update(@Body() editorDto: EditorRequestDto): Promise<EditorResponseDto> {
    return this.editorsService.update(editorDto);
  }

  @Delete(':id')
  @HttpCode(204)
  async delete(@Param('id') id: number): Promise<void> {
    await this.editorsService.delete(id);
  }
}
