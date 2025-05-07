import { Entity, PrimaryGeneratedColumn, Column, ManyToMany } from 'typeorm';
import { Issue } from './issue.entity.js';

@Entity('tbl_marker')
export class Marker {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ length: 64,type: 'varchar' })
    name: string;

    @ManyToMany(() => Issue, (issue) => issue.markers)
    issues: Issue[];
}