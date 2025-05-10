import { Mark } from '../entities/mark.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class MarkService {
    constructor() {
        this.markRepository = getRepository(Mark);
    }
    async create(markDto) {
        const errors = await validate(markDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const mark = this.markRepository.create(markDto);
        await this.markRepository.save(mark);
        return {
            id: mark.id,
            name: mark.name,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [marks] = await this.markRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return marks.map(mark => ({
            id: mark.id,
            name: mark.name,
        }));
    }
    async findById(id) {
        const mark = await this.markRepository.findOne({ where: { id } });
        if (mark) {
            return {
                id: mark.id,
                name: mark.name,
            };
        }
        return undefined;
    }
    async update(id, markDto) {
        const errors = await validate(markDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const mark = await this.markRepository.findOne({ where: { id } });
        if (mark) {
            Object.assign(mark, markDto);
            await this.markRepository.save(mark);
            return {
                id: mark.id,
                name: mark.name,
            };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.markRepository.delete(id);
        return result.affected > 0;
    }
}
