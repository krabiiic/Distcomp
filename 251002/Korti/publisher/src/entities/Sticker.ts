import { Column, Entity, ManyToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Article } from './Article';

@Entity({ name: 'tbl_sticker' })
export class Sticker {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @ManyToMany(() => Article, { onDelete: 'CASCADE' })
  articles: Article[];
}
