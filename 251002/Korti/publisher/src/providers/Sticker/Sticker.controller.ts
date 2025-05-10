import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  HttpStatus,
  Param,
  ParseIntPipe,
  Post,
  Put,
} from '@nestjs/common';
import { StickerService } from './Sticker.service';
import { StickerRequestTo, UpdateStickerTo } from './Dto/StickerRequestTo';

@Controller('api/v1.0/stickers')
export class StickerController {
  constructor(private readonly stickerService: StickerService) {}

  @Get()
  @HttpCode(HttpStatus.OK)
  async getAll() {
    return await this.stickerService.getAllStickers();
  }

  @Post()
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() body: StickerRequestTo) {
    return await this.stickerService.createSticker(body);
  }

  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  async delete(@Param('id', ParseIntPipe) stickerId: number) {
    await this.stickerService.deleteSticker(stickerId);
  }

  @Get(':id')
  @HttpCode(HttpStatus.OK)
  async getOne(@Param('id', ParseIntPipe) stickerId: number) {
    return await this.stickerService.getStickerById(stickerId);
  }

  @Put()
  @HttpCode(HttpStatus.OK)
  async update(@Body() req: UpdateStickerTo) {
    return await this.stickerService.updateSticker(req);
  }

  @Get('articles/:stickerId')
  @HttpCode(HttpStatus.OK)
  async getByArticles(@Param('id', ParseIntPipe) id: number) {
    return await this.stickerService.getStickersByArticleId(id);
  }
}
