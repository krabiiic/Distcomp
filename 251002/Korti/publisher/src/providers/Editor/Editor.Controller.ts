import {
  Body,
  Controller,
  Delete,
  Get,
  HttpStatus,
  Param,
  ParseIntPipe,
  Put,
  Post,
  HttpCode,
} from '@nestjs/common';
import { EditorService } from './Editor.Service';
import { EditorResponseTo } from './Dto/EditorResponseTo';
import { EditorRequestTo, UpdateEditorDto } from './Dto/EditorRequestTo';

@Controller('api/v1.0/editors')
export class EditorController {
  constructor(private readonly editorService: EditorService) {}

  @Get()
  @HttpCode(HttpStatus.OK)
  async findAll() {
    const editors = await this.editorService.getAllEditors();
    return editors;
  }

  @Get(':id')
  @HttpCode(HttpStatus.OK)
  async findById(@Param('id', ParseIntPipe) idEditor: number) {
    const editor: EditorResponseTo =
      await this.editorService.findById(idEditor);
    return editor;
  }

  @Post()
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() req: EditorRequestTo) {
    const editor: EditorResponseTo = await this.editorService.createEditor(req);
    return editor;
  }

  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  async delete(@Param('id', ParseIntPipe) idEditor: number): Promise<void> {
    await this.editorService.deleteEditor(idEditor);
  }

  @Put()
  @HttpCode(HttpStatus.OK)
  async update(@Body() body: UpdateEditorDto) {
    const editor = await this.editorService.updateEditor(body);
    return editor;
  }

  @Get('article/:articleId')
  @HttpCode(HttpStatus.OK)
  async getByarticleId(@Param('articleId', ParseIntPipe) articleId: number) {
    const editor = await this.editorService.getEditorByArticleId(articleId);
    return editor;
  }
}
