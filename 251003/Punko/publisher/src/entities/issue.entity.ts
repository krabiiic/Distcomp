import {
    PrimaryGeneratedColumn,
    Column,
    ManyToOne,
    OneToMany,
    ManyToMany,
    JoinTable,
    Entity, JoinColumn,
} from 'typeorm';
import { Author } from './author.entity.js';
import { Reaction } from './reaction.entity.js';
import { Marker } from './marker.entity.js';

@Entity('tbl_issue')
export class Issue {
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

    @ManyToOne(() => Author, (author) => author)
    @JoinColumn({ name: 'author_id' })
    author: Author;


    @ManyToMany(() => Marker)
    @JoinTable({ name: 'tbl_issue_marker' })
    markers: Marker[];
}