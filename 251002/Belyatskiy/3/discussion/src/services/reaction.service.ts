import { MessageRepository } from '../repositories/message.repository.js';
import { MessageRequestTo, MessageResponseTo } from '../dtos/message.dto.js';
import { validate } from 'class-validator';
import {Message} from "../entities/message.entity";

export class MessageService {
    private messageRepository = new MessageRepository();

    async create(messageDto: MessageRequestTo): Promise<MessageResponseTo> {
        const errors = await validate(messageDto);
        if (errors.length > 0) throw new Error('Validation failed');

        // Генерируем ID в пределах int
        const lastMessages = await this.messageRepository.findAll();
        const newId = lastMessages.length > 0
            ? Math.max(...lastMessages.map(r => r.id)) + 1
            : 1;
        const message = { id: newId,content: messageDto.content, articleid: messageDto.articleId };
        await this.messageRepository.create(message);
        return { id: message.id, content: message.content, articleId: message.articleid };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<MessageResponseTo[]> {
        const result = await this.messageRepository.findAll();
        const sorted = result.sort((a, b) => {
            const valueA = sortBy === 'articleId' ? a.articleid : a[sortBy as keyof Message];
            const valueB = sortBy === 'articleId' ? b.articleid : b[sortBy as keyof Message];
            if (order === 'ASC') {
                return valueA > valueB ? 1 : -1;
            }
            return valueA < valueB ? 1 : -1;
        });
        const offset = (page - 1) * limit;
        return sorted.slice(offset, offset + limit).map(r => ({
            id: r.id,
            content: r.content,
            articleId: r.articleid, // Маппим articleid в articleId
        }));
    }

    async findById(id: number): Promise<MessageResponseTo | undefined> {
        const message = await this.messageRepository.findById(id);
        if (!message) return undefined;
        return {
            id: message.id,
            content: message.content,
            articleId: message.articleid // Маппим articleid в articleId
        };
    }

    async update(messageDto: MessageRequestTo): Promise<MessageResponseTo | undefined> {
        const errors = await validate(messageDto);
        if (errors.length > 0) {
            throw new Error(`Validation failed: ${errors.join(', ')}`);
        }
        if (!messageDto.id) {
            throw new Error('ID is required');
        }
        const message = {
            id: messageDto.id,
            content: messageDto.content,
            articleid: messageDto.articleId // Маппим articleId в articleid
        };
        const updated = await this.messageRepository.update(message);
        if (!updated) return undefined;
        return {
            id: updated.id,
            content: updated.content,
            articleId: updated.articleid // Маппим articleid в articleId
        };
    }

    async delete(id: number): Promise<boolean> {
        return this.messageRepository.delete(id);
    }
}