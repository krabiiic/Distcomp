import {
  ConflictException,
  HttpException,
  HttpStatus,
  Injectable,
  InternalServerErrorException,
} from '@nestjs/common';
import { Sticker } from '../../entities/Sticker';
import { StickerRequestTo, UpdateStickerTo } from './Dto/StickerRequestTo';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { StickerResponseTo } from './Dto/StickerResponseTo';
import { plainToInstance } from 'class-transformer';
import { Article } from '../../entities/Article';

@Injectable()
export class StickerService {
  constructor(
    @InjectRepository(Sticker)
    private readonly stickerRepository: Repository<Sticker>,
    @InjectRepository(Article)
    private readonly articleRepository: Repository<Article>,
  ) {}

  async getAllStickers(): Promise<ReadonlyArray<StickerResponseTo>> {
    return plainToInstance(
      StickerResponseTo,
      await this.stickerRepository.find(),
      {
        excludeExtraneousValues: true,
      },
    );
  }

  async createSticker(sticker: StickerRequestTo): Promise<StickerResponseTo> {
    try {
      const st = this.stickerRepository.create(sticker);
      return plainToInstance(
        StickerResponseTo,
        await this.stickerRepository.save(st),
        { excludeExtraneousValues: true },
      );
    } catch (err) {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      if ((err.code as string) === '23505') {
        throw new HttpException(
          {
            errorCode: 40005,
            errorMessage: 'Sticker with this name already exist.',
          },
          HttpStatus.FORBIDDEN,
        );
      }
      throw InternalServerErrorException;
    }
  }

  async deleteSticker(id: number): Promise<void> {
    try {
      const sticker = await this.stickerRepository.findOne({ where: { id } });
      if (!sticker) throw new ConflictException();
      await this.stickerRepository.delete(id);
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40006,
            errorMessage: 'Sticker does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      }
      throw new InternalServerErrorException();
    }
  }

  async getStickerById(id: number): Promise<StickerResponseTo> {
    try {
      const sticker = await this.stickerRepository.findOne({ where: { id } });
      if (!sticker) throw new ConflictException();
      return plainToInstance(StickerResponseTo, sticker, {
        excludeExtraneousValues: true,
      });
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40006,
            errorMessage: 'Sticker does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      }
      throw new InternalServerErrorException();
    }
  }

  async updateSticker(item: UpdateStickerTo): Promise<StickerResponseTo> {
    try {
      const st = await this.stickerRepository.findOne({
        where: { id: item.id },
      });
      if (!st) throw new ConflictException();
      await this.stickerRepository.update(item.id, {
        name: item.name,
      });
      const updSticker = await this.stickerRepository.findOne({
        where: { id: item.id },
      });
      if (!updSticker) throw new Error();
      return plainToInstance(StickerResponseTo, updSticker, {
        excludeExtraneousValues: true,
      });
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40006,
            errorMessage: 'Sticker does not exist.',
          },
          HttpStatus.BAD_REQUEST,
        );
      }
      throw new InternalServerErrorException();
    }
  }

  async getStickersByArticleId(id: number): Promise<StickerResponseTo[]> {
    try {
      const article = await this.articleRepository.findOne({ where: { id } });
      if (!article) throw new ConflictException();
      return plainToInstance(StickerResponseTo, article.stickers, {
        excludeExtraneousValues: true,
      });
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40002,
            errorMessage: 'Article does not exist.',
          },
          HttpStatus.BAD_REQUEST,
        );
      }
      throw new InternalServerErrorException();
    }
  }
}
