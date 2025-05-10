import { ReactionRepository } from '../repositories/reaction.repository.js';
import { ReactionRequestTo, ReactionResponseTo } from '../dtos/reaction.dto.js';
import { validate } from 'class-validator';
import {Reaction} from "../entities/reaction.entity";

export class ReactionService {
    private reactionRepository = new ReactionRepository();

    async create(reactionDto: ReactionRequestTo): Promise<ReactionResponseTo> {
        const errors = await validate(reactionDto);
        if (errors.length > 0) throw new Error('Validation failed');

        // Генерируем ID в пределах int
        const lastReactions = await this.reactionRepository.findAll();
        const newId = lastReactions.length > 0
            ? Math.max(...lastReactions.map(r => r.id)) + 1
            : 1;
        const reaction = { id: newId,content: reactionDto.content, issueid: reactionDto.issueId };
        await this.reactionRepository.create(reaction);
        return { id: reaction.id, content: reaction.content, issueId: reaction.issueid };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<ReactionResponseTo[]> {
        const result = await this.reactionRepository.findAll();
        const sorted = result.sort((a, b) => {
            const valueA = sortBy === 'issueId' ? a.issueid : a[sortBy as keyof Reaction];
            const valueB = sortBy === 'issueId' ? b.issueid : b[sortBy as keyof Reaction];
            if (order === 'ASC') {
                return valueA > valueB ? 1 : -1;
            }
            return valueA < valueB ? 1 : -1;
        });
        const offset = (page - 1) * limit;
        return sorted.slice(offset, offset + limit).map(r => ({
            id: r.id,
            content: r.content,
            issueId: r.issueid, // Маппим issueid в issueId
        }));
    }

    async findById(id: number): Promise<ReactionResponseTo | undefined> {
        const reaction = await this.reactionRepository.findById(id);
        if (!reaction) return undefined;
        return {
            id: reaction.id,
            content: reaction.content,
            issueId: reaction.issueid // Маппим issueid в issueId
        };
    }

    async update(reactionDto: ReactionRequestTo): Promise<ReactionResponseTo | undefined> {
        const errors = await validate(reactionDto);
        if (errors.length > 0) {
            throw new Error(`Validation failed: ${errors.join(', ')}`);
        }
        if (!reactionDto.id) {
            throw new Error('ID is required');
        }
        const reaction = {
            id: reactionDto.id,
            content: reactionDto.content,
            issueid: reactionDto.issueId // Маппим issueId в issueid
        };
        const updated = await this.reactionRepository.update(reaction);
        if (!updated) return undefined;
        return {
            id: updated.id,
            content: updated.content,
            issueId: updated.issueid // Маппим issueid в issueId
        };
    }

    async delete(id: number): Promise<boolean> {
        return this.reactionRepository.delete(id);
    }
}