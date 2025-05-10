import { Request, Response } from 'express';
import { MessageService } from '../services/message.service.js';
import { MessageRequestTo } from '../dtos/message.dto.js';

export class MessageController {
    private messageService = new MessageService();

    create = async (req: Request, res: Response) => {
        try {
            const messageDto: MessageRequestTo = req.body;
            const message = await this.messageService.create(messageDto);
            res.status(201).json(message);
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

            const messages = await this.messageService.findAll(page, limit, sortBy, order);
            res.status(200).json(messages);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const message = await this.messageService.findById(id);
            if (message) {
                res.status(200).json(message);
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
            const message = await this.messageService.update(messageDto);
            if (message) {
                res.status(200).json(message);
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
            const isDeleted = await this.messageService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Message not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}