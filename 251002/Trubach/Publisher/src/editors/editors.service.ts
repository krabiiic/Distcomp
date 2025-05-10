import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { Editor } from './editor.entity';
import { EditorRequestDto, EditorResponseDto } from '../common/dto/editor.dto';
import { DatabaseEditorRepository } from './repositories/editor.repository';

@Injectable()
export class EditorsService {
  constructor(private readonly editorRepository: DatabaseEditorRepository) {}

  async create(editorDto: EditorRequestDto): Promise<EditorResponseDto> {
    editorDto = this.validateEditor(editorDto);
    const editor = await this.editorRepository.create(editorDto as Editor);
    return this.toResponseDto(editor);
  }

  async findAll(): Promise<EditorResponseDto[]> {
    return (await this.editorRepository.findAll()).map((v) => this.toResponseDto(v));
  }
  async findOne(id: number): Promise<EditorResponseDto> {
    const editor = await this.editorRepository.findOne(id);
    if (!editor) throw new NotFoundException(`Editor with ID ${id} not found`);
    return this.toResponseDto(editor);
  }

  async update(editorDto: EditorRequestDto): Promise<EditorResponseDto> {
    this.validateEditor(editorDto);
    // @ts-ignore
    const id = parseInt(editorDto.id);
    const editor = await this.editorRepository.update(editorDto as Editor);
    if (!editor) throw new NotFoundException(`Editor with ID ${id} not found`);
    return this.toResponseDto(editor);
  }

  async delete(id: number): Promise<void> {
    const deleted = await this.editorRepository.delete(id);
    if (!deleted) throw new NotFoundException(`Editor with ID ${id} not found`);
  }

  private toResponseDto(editor: Editor): EditorResponseDto {
    return {
      id: editor.id,
      login: editor.login,
      password: editor.password,
      firstname: editor.firstname,
      lastname: editor.lastname,
    };
  }

  private validateEditor(
    editorDto: EditorRequestDto | EditorResponseDto,
  ): EditorRequestDto | EditorResponseDto {
    if (
      editorDto.login.length < 2 ||
      editorDto.login.length > 64 ||
      editorDto.password.length < 8 ||
      editorDto.password.length > 128 ||
      editorDto.firstname.length < 2 ||
      editorDto.firstname.length > 64 ||
      editorDto.lastname.length < 2 ||
      editorDto.lastname.length > 64
    ) {
      throw new HttpException(
        'Check lengths',
        HttpStatus.NOT_ACCEPTABLE,
      );
    }
    return editorDto;
  }
}
