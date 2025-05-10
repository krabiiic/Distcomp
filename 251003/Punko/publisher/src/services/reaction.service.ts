import { ReactionRequestTo, ReactionResponseTo } from '../dtos/reaction.dto.js';
import { Reaction } from '../entities/reaction.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {Issue} from "../entities/issue.entity";
import {cacheService} from "./cache.service";

const REACTION_CACHE_PREFIX = 'reaction';
const REACTION_FIND_BY_ID_PREFIX = `${REACTION_CACHE_PREFIX}:findById`;
const REACTION_FIND_ALL_PREFIX = `${REACTION_CACHE_PREFIX}:findAll`;

export class ReactionService {
    private reactionRepository = getRepository(Reaction);

    async create(reactionDto: ReactionRequestTo): Promise<ReactionResponseTo> {
        const errors = await validate(reactionDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const issue = await getRepository(Issue).findOne({ where: { id: reactionDto.issueId } });
        if (!issue) {
            const error = new Error('issue not found');
            (error as any).status = 404;
            throw error;
        }
        const reactionToSave = {
            ...reactionDto,
            issue
        }
        const reaction = this.reactionRepository.create(reactionToSave);
        await this.reactionRepository.save(reaction);
        await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
        return {
            id: reaction.id,
            content: reaction.content,
            issueId: reaction.issue.id,
        };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<ReactionResponseTo[] | undefined | null> {
        const cacheKey = REACTION_FIND_ALL_PREFIX;
        return await cacheService.getOrSet<ReactionResponseTo[]>(cacheKey, async () => {
            const [reactions] = await this.reactionRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
                relations: ['issue'],
            });
            return reactions.map(reaction => ({
                id: reaction.id,
                content: reaction.content,
                issueId: reaction.issue?.id,
            }));
        });
    }

    async findById(id: number): Promise<ReactionResponseTo | undefined | null> {
        const cacheKey = `${REACTION_FIND_BY_ID_PREFIX}:${id}`;
        return await cacheService.getOrSet<ReactionResponseTo>(cacheKey, async () => {
            const reaction = await this.reactionRepository.findOne({where: {id}, relations: ['issue']});
            if (reaction) {
                return {
                    id: reaction.id,
                    content: reaction.content,
                    issueId: reaction.issue.id,
                };
            }
            return undefined;
        });
    }

    async update(reactionDto: ReactionRequestTo): Promise<ReactionResponseTo | undefined> {
        const errors = await validate(reactionDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        if (!reactionDto.id) {
            const error = new Error('Reaction ID is required for update');
            (error as any).status = 400;
            throw error;
        }

        const reaction = await this.reactionRepository.findOne({
            where: { id: reactionDto.id },
            relations: ['issue']
        });
        if (!reaction) {
            const error = new Error('Reaction not found');
            (error as any).status = 404;
            throw error;
        }

        const issue = await getRepository(Issue).findOne({ where: { id: reactionDto.issueId } });
        if (!issue) {
            const error = new Error('Issue with this ID does not exist');
            (error as any).status = 404;
            throw error;
        }

        reaction.content = reactionDto.content;
        reaction.issue = issue;

        await this.reactionRepository.save(reaction);
        await cacheService.invalidate(`${REACTION_FIND_BY_ID_PREFIX}:${reaction.id}`);
        await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
        return {
            id: reaction.id,
            content: reaction.content,
            issueId: reaction.issue.id,
        };
    }

    async delete(id: number): Promise<boolean> {
        const result = await this.reactionRepository.delete(id);
        await cacheService.invalidate(`${REACTION_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}