import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Issue } from './issue.entity.js';

@Entity('tbl_reaction')
export class Reaction {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ type: 'text' })
    content: string;

    @ManyToOne(() => Issue, (issue) => issue.reactions)
    issue: Issue;
}