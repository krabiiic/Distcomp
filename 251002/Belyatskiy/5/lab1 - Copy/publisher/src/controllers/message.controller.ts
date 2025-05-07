import { Request, Response } from 'express';
import { MessageService } from '../services/message.service.js';
import {MessageRequestTo, MessageResponseTo} from '../dtos/message.dto.js';
import axios from "axios";
import {cacheService} from "../services/cache.service.js";

const REACTION_CACHE_PREFIX = 'message';
const REACTION_FIND_BY_ID_PREFIX = `${REACTION_CACHE_PREFIX}:findById`;
const REACTION_FIND_ALL_PREFIX = `${REACTION_CACHE_PREFIX}:findAll`;

export class MessageController {
    private messageService = new MessageService();
    private discussionApi = 'http://localhost:24130/api/v1.0/messages';

    create = async (req: Request, res: Response) => {
        try {
            const messageDto: MessageRequestTo = req.body;
            //const message = await this.messageService.create(messageDto);
            const message = await axios.post(this.discussionApi, messageDto);
            await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
            res.status(201).json(message.data);
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
            const messagesResult = await cacheService.getOrSet<MessageResponseTo[]>(cacheKey, async () => {
                const response = await axios.get(this.discussionApi);
                return response.data as MessageResponseTo[];
            })
            res.status(200).json(messagesResult); // Передаём только данные
        } catch (error: any) {
            console.error('Error fetching messages:', error.message);
            res.status(500).json({ message: error.message || 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const cacheKey = `${REACTION_FIND_BY_ID_PREFIX}:${id}`;
            const messagesResult = await cacheService.getOrSet<MessageResponseTo>(cacheKey, async () => {
                const message = await axios.get(`${this.discussionApi}/${id}`);
                //const message = await this.messageService.findById(id);
                return message.data as MessageResponseTo;
            });
            if (messagesResult) {
                res.status(200).json(messagesResult);
            } else {
                res.status(404).json({ message: 'Message not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const messageDto: MessageRequestTo = req.body;
            const message = await axios.put( `${this.discussionApi}`,messageDto);
            if (message.data) {
                await cacheService.invalidate(`${REACTION_FIND_BY_ID_PREFIX}:${messageDto.id}`);
                await cacheService.invalidatePattern(`${REACTION_FIND_ALL_PREFIX}*`);
                res.status(200).json(message.data);
            } else {
                res.status(404).json({ message: 'Message not found' });
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
                res.status(404).json({ message: 'Message not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}