import { MarkRequestTo, MarkResponseTo } from '../dtos/mark.dto.js';
import { Mark } from '../entities/mark.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';

export class MarkService {
    private markRepository = getRepository(Mark);

    async create(markDto: MarkRequestTo): Promise<MarkResponseTo> {
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

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<MarkResponseTo[]> {
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

    async findById(id: number): Promise<MarkResponseTo | undefined> {
        const mark = await this.markRepository.findOne({ where: { id } });
        if (mark) {
            return {
                id: mark.id,
                name: mark.name,
            };
        }
        return undefined;
    }

    async update(id: number, markDto: MarkRequestTo): Promise<MarkResponseTo | undefined> {
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

    async delete(id: number): Promise<boolean> {
        const result = await this.markRepository.delete(id);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}