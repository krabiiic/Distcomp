import { Reaction } from '../entities/reaction.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class ReactionService {
    constructor() {
        this.reactionRepository = getRepository(Reaction);
    }
    async create(reactionDto) {
        const errors = await validate(reactionDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const reaction = this.reactionRepository.create(reactionDto);
        await this.reactionRepository.save(reaction);
        return {
            id: reaction.id,
            content: reaction.content,
            issueId: reaction.issue.id,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [reactions] = await this.reactionRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return reactions.map(reaction => ({
            id: reaction.id,
            content: reaction.content,
            issueId: reaction.issue.id,
        }));
    }
    async findById(id) {
        const reaction = await this.reactionRepository.findOne({ where: { id }, relations: ['issue'] });
        if (reaction) {
            return {
                id: reaction.id,
                content: reaction.content,
                issueId: reaction.issue.id,
            };
        }
        return undefined;
    }
    async update(id, reactionDto) {
        const errors = await validate(reactionDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const reaction = await this.reactionRepository.findOne({ where: { id } });
        if (reaction) {
            Object.assign(reaction, reactionDto);
            await this.reactionRepository.save(reaction);
            return {
                id: reaction.id,
                content: reaction.content,
                issueId: reaction.issue.id,
            };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.reactionRepository.delete(id);
        return result.affected > 0;
    }
}
