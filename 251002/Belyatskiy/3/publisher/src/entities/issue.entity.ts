import {
    PrimaryGeneratedColumn,
    Column,
    ManyToOne,
    OneToMany,
    ManyToMany,
    JoinTable,
    Entity, JoinColumn,
} from 'typeorm';
import { User } from './user.entity.js';
import { Message } from './message.entity.js';
import { Mark } from './mark.entity.js';

@Entity('tbl_article')
export class Article {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ length: 64,type: 'varchar' })
    title: string;

    @Column({type: 'text'})
    content: string;

    @Column({ type: 'bigint' })
    created: number;

    @Column({ type: 'bigint' })
    modified: number;

    @ManyToOne(() => User, (user) => user)
    @JoinColumn({ name: 'user_id' })
    user: User;


    @ManyToMany(() => Mark)
    @JoinTable({ name: 'tbl_article_mark' })
    marks: Mark[];
}