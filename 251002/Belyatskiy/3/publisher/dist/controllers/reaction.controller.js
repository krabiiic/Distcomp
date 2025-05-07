import { MessageService } from '../services/message.service';
export class MessageController {
    constructor() {
        this.messageService = new MessageService();
        this.create = async (req, res) => {
            try {
                const messageDto = req.body;
                const message = await this.messageService.create(messageDto);
                res.status(201).json(message);
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.findAll = async (req, res) => {
            try {
                const page = parseInt(req.query.page, 10) || 1;
                const limit = parseInt(req.query.limit, 10) || 10;
                const sortBy = req.query.sortBy || 'id';
                const order = req.query.order || 'ASC';
                const messages = await this.messageService.findAll(page, limit, sortBy, order);
                res.status(200).json(messages);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const message = await this.messageService.findById(id);
                if (message) {
                    res.status(200).json(message);
                }
                else {
                    res.status(404).json({ message: 'Message not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const messageDto = req.body;
                const message = await this.messageService.update(id, messageDto);
                if (message) {
                    res.status(200).json(message);
                }
                else {
                    res.status(404).json({ message: 'Message not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.messageService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Message not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
