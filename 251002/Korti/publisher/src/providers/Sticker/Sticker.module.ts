import { Module } from '@nestjs/common';
import { StickerController } from './Sticker.controller';
import { StickerService } from './Sticker.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Sticker } from 'src/entities/Sticker';
import { Article } from 'src/entities/Article';

@Module({
  imports: [TypeOrmModule.forFeature([Sticker, Article])],
  providers: [StickerService],
  controllers: [StickerController],
})
export class StickerModule {}
