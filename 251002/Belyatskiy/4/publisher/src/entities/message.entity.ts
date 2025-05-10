import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Article } from './article.entity.js';

@Entity('tbl_message')
export class Message {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ type: 'text' })
    content: string;

    @ManyToOne(() => Article, (article) => article.messages)
    article: Article;
}