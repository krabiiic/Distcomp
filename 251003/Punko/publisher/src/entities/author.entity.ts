import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { Issue } from './issue.entity.js';

@Entity('tbl_author')
export class Author {
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

    @OneToMany(() => Issue, (issue) => issue.author)
    issues: Issue[];
}