import { Message } from '../entities/message.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class MessageService {
    constructor() {
        this.messageRepository = getRepository(Message);
    }
    async create(messageDto) {
        const errors = await validate(messageDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const message = this.messageRepository.create(messageDto);
        await this.messageRepository.save(message);
        return {
            id: message.id,
            content: message.content,
            articleId: message.article.id,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [messages] = await this.messageRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return messages.map(message => ({
            id: message.id,
            content: message.content,
            articleId: message.article.id,
        }));
    }
    async findById(id) {
        const message = await this.messageRepository.findOne({ where: { id }, relations: ['article'] });
        if (message) {
            return {
                id: message.id,
                content: message.content,
                articleId: message.article.id,
            };
        }
        return undefined;
    }
    async update(id, messageDto) {
        const errors = await validate(messageDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const message = await this.messageRepository.findOne({ where: { id } });
        if (message) {
            Object.assign(message, messageDto);
            await this.messageRepository.save(message);
            return {
                id: message.id,
                content: message.content,
                articleId: message.article.id,
            };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.messageRepository.delete(id);
        return result.affected > 0;
    }
}
