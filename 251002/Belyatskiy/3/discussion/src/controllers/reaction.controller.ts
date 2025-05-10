import { MessageService } from '../services/message.service.js';
import { MessageRequestTo } from '../dtos/message.dto.js';

export class MessageController {
    private messageService = new MessageService();

    async create(req: any, res: any, next: any) {
        try {
            const messageDto: MessageRequestTo = req.body;
            const result = await this.messageService.create(messageDto);
            res.status(201).json(result);
        } catch (error) {
            next(error);
        }
    }

    async findAll(req: any, res: any, next: any) {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';
            const result = await this.messageService.findAll(page, limit, sortBy, order);
            res.status(200).json(result || []);
        } catch (error) {
            next(error);
        }
    }

    async findById(req: any, res: any, next: any) {
        try {
            const id = parseInt(req.params.id);
            if (isNaN(id)) {
                return res.status(400).send('Invalid ID');
            }
            const result = await this.messageService.findById(id);
            if (result) {
                res.status(200).json(result);
            } else {
                res.status(404).send('Message not found');
            }
        } catch (error) {
            next(error);
        }
    }

    async update(req: any, res: any, next: any) {
        try {
            const messageDto: MessageRequestTo = req.body;
            console.log('Updating message with DTO:', messageDto);
            const result = await this.messageService.update(messageDto);
            console.log('Updated message:', result);
            if (result) {
                res.status(200).json(result);
            } else {
                console.log('Message not found');
                res.status(404).send('Message not found');
            }
        } catch (error) {
            next(error);
        }
    }

    async delete(req: any, res: any, next: any) {
        try {
            const id = parseInt(req.params.id);
            if (isNaN(id)) {
                return res.status(400).send('Invalid ID');
            }
            console.log("Удаление: "+id)
            const success = await this.messageService.delete(id);

            res.status(success ? 204 : 404).send();
        } catch (error) {
            next(error);
        }
    }
}