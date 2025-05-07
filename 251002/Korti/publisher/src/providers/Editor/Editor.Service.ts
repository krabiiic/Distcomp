import {
  ConflictException,
  HttpException,
  HttpStatus,
  Injectable,
  InternalServerErrorException,
  NotFoundException,
} from '@nestjs/common';
import { Editor } from '../../entities/Editor';
import { EditorRequestTo, UpdateEditorDto } from './Dto/EditorRequestTo';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Article } from '../../entities/Article';
import { EditorResponseTo } from './Dto/EditorResponseTo';
import { plainToInstance } from 'class-transformer';

@Injectable()
export class EditorService {
  constructor(
    @InjectRepository(Editor)
    private editorRepository: Repository<Editor>,
    @InjectRepository(Article)
    private articleRepository: Repository<Article>,
  ) {}

  async getAllEditors(): Promise<ReadonlyArray<EditorResponseTo>> {
    return plainToInstance(
      EditorResponseTo,
      await this.editorRepository.find(),
      { excludeExtraneousValues: true },
    );
  }

  async createEditor(editor: EditorRequestTo): Promise<EditorResponseTo> {
    try {
      const newEditor = this.editorRepository.create(editor);
      return plainToInstance(
        EditorResponseTo,
        await this.editorRepository.save(newEditor),
        { excludeExtraneousValues: true },
      );
    } catch (err) {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      if ((err.code as string) === '23505') {
        throw new HttpException(
          {
            errorCode: 40001,
            errorMessage: 'Editor already exist.',
          },
          HttpStatus.FORBIDDEN,
        );
      }
      throw new InternalServerErrorException('Unexpected error');
    }
  }

  async deleteEditor(id: number): Promise<void> {
    try {
      const editor = await this.editorRepository.findOneBy({ id });
      if (!editor) throw new ConflictException();
      await this.editorRepository.delete(id);
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40400,
            errorMessage: 'Editor does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      }
      throw new InternalServerErrorException('Unexpected error');
    }
  }

  async findById(id: number): Promise<EditorResponseTo> {
    try {
      const editor = await this.editorRepository.findOneBy({ id });
      if (editor)
        return plainToInstance(EditorResponseTo, editor, {
          excludeExtraneousValues: true,
        });
      else
        throw new HttpException(
          {
            errorCode: 40400,
            errorMessage: 'Editor does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
    } catch {
      throw new InternalServerErrorException('Unexpected error');
    }
  }

  async updateEditor(item: UpdateEditorDto): Promise<EditorResponseTo> {
    try {
      const existEditor = await this.editorRepository.findOne({
        where: { id: item.id },
      });
      if (!existEditor) {
        throw new HttpException(
          {
            errorCode: 40400,
            errorMessage: 'Editor does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      }
      await this.editorRepository.update(item.id, {
        firstname: item.firstname,
        lastname: item.lastname,
        login: item.login,
        password: item.password,
      });
      const result = await this.editorRepository.findOne({
        where: { id: item.id },
      });
      if (!result) throw new NotFoundException();
      return plainToInstance(EditorResponseTo, result, {
        excludeExtraneousValues: true,
      });
    } catch {
      throw new InternalServerErrorException('Unexpected error');
    }
  }

  async getEditorByArticleId(articleId: number): Promise<EditorResponseTo> {
    try {
      const article = await this.articleRepository.findOne({
        where: { id: articleId },
      });
      if (!article) throw new ConflictException();
      const editor = await this.editorRepository.findOne({
        where: { id: article.editorId },
      });
      if (!editor) throw new NotFoundException();
      return plainToInstance(EditorResponseTo, editor, {
        excludeExtraneousValues: true,
      });
    } catch (err) {
      if (err instanceof ConflictException) {
        throw new HttpException(
          {
            errorCode: 40402,
            errorMessage: 'Article does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      } else if (err instanceof NotFoundException) {
        throw new HttpException(
          {
            errorCode: 40400,
            errorMessage: 'Editor does not exist.',
          },
          HttpStatus.NOT_FOUND,
        );
      }
      throw new InternalServerErrorException('Unexpected error');
    }
  }
}
