import { Entity, PrimaryGeneratedColumn, Column, ManyToMany } from 'typeorm';
import { Article } from './article.entity.js';

@Entity('tbl_mark')
export class Mark {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ length: 64,type: 'varchar' })
    name: string;

    @ManyToMany(() => Article, (article) => article.marks)
    articles: Article[];
}