import { UserRequestTo, UserResponseTo } from '../dtos/user.dto.js';
import { User } from '../entities/user.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {cacheService} from "./cache.service.js";

const AUTHOR_CACHE_PREFIX = 'user';
const AUTHOR_FIND_BY_ID_PREFIX = `${AUTHOR_CACHE_PREFIX}:findById`;
const AUTHOR_FIND_ALL_PREFIX = `${AUTHOR_CACHE_PREFIX}:findAll`;

export class UserService {
    private userRepository = getRepository(User);
    // Определяем префиксы для ключей кэша

    async create(userDto: UserRequestTo): Promise<UserResponseTo> {
        const errors = await validate(userDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const existingUser = await this.userRepository.findOne({ where: { login: userDto.login } });
        if (existingUser) {
            const error = new Error('User with this login already exists');
            (error as any).status = 403;
            throw error;
        }
        const user = this.userRepository.create(userDto);
        await this.userRepository.save(user);
        await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
        return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<UserResponseTo[] | undefined | null> {
        const cacheKey = AUTHOR_FIND_ALL_PREFIX;
        const result =  cacheService.getOrSet<UserResponseTo[]>(cacheKey, async () => {
            const [users] = await this.userRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
            });
            return users.map(user => ({
                id: user.id,
                login: user.login,
                firstname: user.firstname,
                lastname: user.lastname,
            }));
        });
        return result ?? [];
    }

    async findById(id: number): Promise<UserResponseTo | undefined | null> {
        const cacheKey = `${AUTHOR_FIND_BY_ID_PREFIX}:${id}`;
        const result = cacheService.getOrSet<UserResponseTo>(cacheKey, async () => {
        const user = await this.userRepository.findOne({ where: { id } });
        if (user) {
            return {
                id: user.id,
                login: user.login,
                firstname: user.firstname,
                lastname: user.lastname,
            };
        }
        return undefined;
        });
        return  result === null ? undefined : result;
    }

    async update(id: number, userDto: UserRequestTo): Promise<UserResponseTo | undefined> {
        const errors = await validate(userDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        const user = await this.userRepository.findOne({ where: { id } });
        if (user) {
            Object.assign(user, userDto);
            await this.userRepository.save(user);
            await cacheService.invalidate(`${AUTHOR_FIND_BY_ID_PREFIX}:${id}`);
            await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
            return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
        }
        return undefined;
    }

    async delete(id: number): Promise<boolean> {
        await cacheService.invalidate(`${AUTHOR_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${AUTHOR_FIND_ALL_PREFIX}*`);
        const result = await this.userRepository.delete(id);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}