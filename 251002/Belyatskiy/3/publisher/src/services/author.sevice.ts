import { UserRequestTo, UserResponseTo } from '../dtos/user.dto.js';
import { User } from '../entities/user.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';

export class UserService {
    private userRepository = getRepository(User);

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
        return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<UserResponseTo[]> {
        const [users] = await this.userRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return users.map(user => ({
            id: user.id,
            login: user.login,
            firstname: user.firstname,
            lastname: user.lastname,
        }));
    }

    async findById(id: number): Promise<UserResponseTo | undefined> {
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
            return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
        }
        return undefined;
    }

    async delete(id: number): Promise<boolean> {
        const result = await this.userRepository.delete(id);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}