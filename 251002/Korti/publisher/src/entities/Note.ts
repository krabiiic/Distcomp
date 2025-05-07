import {
  Column,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Article } from './Article';
@Entity({ name: 'tbl_note' })
export class Note {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ nullable: false })
  articleId: number;

  @Column({ nullable: false })
  content: string;

  @ManyToOne(() => Article, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'articleId' })
  article: Article;
}
