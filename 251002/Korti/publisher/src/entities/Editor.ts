import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Article } from './Article';
@Entity({ name: 'tbl_editor' })
export class Editor {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ nullable: false })
  login: string;

  @Column({ nullable: false })
  password: string;

  @Column({ nullable: false })
  firstname: string;

  @Column({ nullable: false })
  lastname: string;

  @OneToMany(() => Article, (article) => article.editor, {
    onDelete: 'CASCADE',
  })
  articles: Article[];
}
