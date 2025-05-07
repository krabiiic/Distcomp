import { EntityRepository, Repository } from 'typeorm';
import { Message } from '../entities/message.entity.js';

@EntityRepository(Message)
export class MessageRepository extends Repository<Message> {}