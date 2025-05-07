import { Author } from '../entities/author.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class AuthorService {
    constructor() {
        this.authorRepository = getRepository(Author);
    }
    async create(authorDto) {
        const errors = await validate(authorDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const author = this.authorRepository.create(authorDto);
        await this.authorRepository.save(author);
        return { id: author.id, login: author.login, firstname: author.firstname, lastname: author.lastname };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [authors] = await this.authorRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return authors.map(author => ({
            id: author.id,
            login: author.login,
            firstname: author.firstname,
            lastname: author.lastname,
        }));
    }
    async findById(id) {
        const author = await this.authorRepository.findOne({ where: { id } });
        if (author) {
            return { id: author.id, login: author.login, firstname: author.firstname, lastname: author.lastname };
        }
        return undefined;
    }
    async update(id, authorDto) {
        const errors = await validate(authorDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const author = await this.authorRepository.findOne({ where: { id } });
        if (author) {
            Object.assign(author, authorDto);
            await this.authorRepository.save(author);
            return { id: author.id, login: author.login, firstname: author.firstname, lastname: author.lastname };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.authorRepository.delete(id);
        return result.affected > 0;
    }
}
