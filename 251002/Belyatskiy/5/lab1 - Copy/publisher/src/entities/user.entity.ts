import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { Article } from './article.entity.js';

@Entity('tbl_user')
export class User {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ length: 64,type: 'varchar' })
    login: string;

    @Column({ length: 128,type: 'varchar' })
    password: string;

    @Column({ length: 64,type: 'varchar' })
    firstname: string;

    @Column({ length: 64,type: 'varchar' })
    lastname: string;

    @OneToMany(() => Article, (article) => article.user)
    articles: Article[];
}