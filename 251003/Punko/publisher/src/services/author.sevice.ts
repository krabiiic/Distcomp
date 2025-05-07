import { AuthorRequestTo, AuthorResponseTo } from '../dtos/author.dto.js';
import { Author } from '../entities/author.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {cacheService} from "./cache.service";

const AUTHOR_CACHE_PREFIX = 'author';
const AUTHOR_FIND_BY_ID_PREFIX = `${AUTHOR_CACHE_PREFIX}:findById`;
const AUTHOR_FIND_ALL_PREFIX = `${AUTHOR_CACHE_PREFIX}:findAll`;

export class AuthorService {
    private authorRepository = getRepository(Author);
    // Определяем префиксы для ключей кэша

    async create(authorDto: AuthorRequestTo): Promise<AuthorResponseTo> {
        const errors = await validate(authorDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const existingAuthor = await this.authorRepository.findOne({ where: { login: authorDto.login } });
        if (existingAuthor) {
            const error = new Error('Author with this login already exists');
            (error as any).status = 403;
            throw error;
        }
        const author = this.authorRepository.create(authorDto);
        await this.authorRepository.save(author);
        await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
        return { id: author.id, login: author.login, firstname: author.firstname, lastname: author.lastname };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<AuthorResponseTo[] | undefined | null> {
        const cacheKey = AUTHOR_FIND_ALL_PREFIX;
        const result =  cacheService.getOrSet<AuthorResponseTo[]>(cacheKey, async () => {
            const [authors] = await this.authorRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
            });
            return authors.map(author => ({
                id: author.id,
                login: author.login,
                firstname: author.firstname,
                lastname: author.lastname,
            }));
        });
        return result ?? [];
    }

    async findById(id: number): Promise<AuthorResponseTo | undefined | null> {
        const cacheKey = `${AUTHOR_FIND_BY_ID_PREFIX}:${id}`;
        const result = cacheService.getOrSet<AuthorResponseTo>(cacheKey, async () => {
        const author = await this.authorRepository.findOne({ where: { id } });
        if (author) {
            return {
                id: author.id,
                login: author.login,
                firstname: author.firstname,
                lastname: author.lastname,
            };
        }
        return undefined;
        });
        return  result === null ? undefined : result;
    }

    async update(id: number, authorDto: AuthorRequestTo): Promise<AuthorResponseTo | undefined> {
        const errors = await validate(authorDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        const author = await this.authorRepository.findOne({ where: { id } });
        if (author) {
            Object.assign(author, authorDto);
            await this.authorRepository.save(author);
            await cacheService.invalidate(`${AUTHOR_FIND_BY_ID_PREFIX}:${id}`);
            await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
            return { id: author.id, login: author.login, firstname: author.firstname, lastname: author.lastname };
        }
        return undefined;
    }

    async delete(id: number): Promise<boolean> {
        await cacheService.invalidate(`${AUTHOR_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
        const result = await this.authorRepository.delete(id);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}