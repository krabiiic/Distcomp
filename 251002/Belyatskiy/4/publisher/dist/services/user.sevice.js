import { User } from '../entities/user.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class UserService {
    constructor() {
        this.userRepository = getRepository(User);
    }
    async create(userDto) {
        const errors = await validate(userDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const user = this.userRepository.create(userDto);
        await this.userRepository.save(user);
        return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
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
    async findById(id) {
        const user = await this.userRepository.findOne({ where: { id } });
        if (user) {
            return { id: user.id, login: user.login, firstname: user.firstname, lastname: user.lastname };
        }
        return undefined;
    }
    async update(id, userDto) {
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
    async delete(id) {
        const result = await this.userRepository.delete(id);
        return result.affected > 0;
    }
}
