import { Request, Response } from 'express';
import { ReactionService } from '../services/reaction.service.js';
import {ReactionRequestTo, ReactionResponseTo} from '../dtos/reaction.dto.js';
import axios from "axios";
import {cacheService} from "../services/cache.service";

const REACTION_CACHE_PREFIX = 'reaction';
const REACTION_FIND_BY_ID_PREFIX = `${REACTION_CACHE_PREFIX}:findById`;
const REACTION_FIND_ALL_PREFIX = `${REACTION_CACHE_PREFIX}:findAll`;

export class ReactionController {
    private reactionService = new ReactionService();
    private discussionApi = 'http://localhost:24130/api/v1.0/reactions';

    create = async (req: Request, res: Response) => {
        try {
            const reactionDto: ReactionRequestTo = req.body;
            //const reaction = await this.reactionService.create(reactionDto);
            const reaction = await axios.post(this.discussionApi, reactionDto);
            await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
            res.status(201).json(reaction.data);
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    findAll = async (req: Request, res: Response) => {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';
            const cacheKey = `${REACTION_FIND_ALL_PREFIX}`
            const reactionsResult = await cacheService.getOrSet<ReactionResponseTo[]>(cacheKey, async () => {
                const response = await axios.get(this.discussionApi);
                return response.data as ReactionResponseTo[];
            })
            res.status(200).json(reactionsResult); // Передаём только данные
        } catch (error: any) {
            console.error('Error fetching reactions:', error.message);
            res.status(500).json({ message: error.message || 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const cacheKey = `${REACTION_FIND_BY_ID_PREFIX}:${id}`;
            const reactionsResult = await cacheService.getOrSet<ReactionResponseTo>(cacheKey, async () => {
                const reaction = await axios.get(`${this.discussionApi}/${id}`);
                //const reaction = await this.reactionService.findById(id);
                return reaction.data as ReactionResponseTo;
            });
            if (reactionsResult) {
                res.status(200).json(reactionsResult);
            } else {
                res.status(404).json({ message: 'Reaction not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const reactionDto: ReactionRequestTo = req.body;
            const reaction = await axios.put( `${this.discussionApi}`,reactionDto);
            if (reaction.data) {
                await cacheService.invalidate(`${REACTION_FIND_BY_ID_PREFIX}:${reactionDto.id}`);
                await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
                res.status(200).json(reaction.data);
            } else {
                res.status(404).json({ message: 'Reaction not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const result = await axios.delete(`${this.discussionApi}/${id}`);
            const isDeleted = result.status === 204;
            if (isDeleted) {
                await cacheService.invalidate(`${REACTION_FIND_BY_ID_PREFIX}:${id}`);
                await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Reaction not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}