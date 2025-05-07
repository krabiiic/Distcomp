import {
  Column,
  Entity,
  JoinColumn,
  JoinTable,
  ManyToMany,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Editor } from './Editor';
import { Sticker } from './Sticker';
import { Note } from './Note';
@Entity({ name: 'tbl_article' })
export class Article {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ name: 'editor_Id', nullable: false })
  editorId: number;

  @Column({ nullable: false })
  title: string;

  @Column({ nullable: false })
  content: string;

  @Column({ type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  created: Date;

  @Column({ type: 'timestamp', nullable: true })
  modified: Date | null;

  @OneToMany(() => Note, (note) => note.article, { onDelete: 'CASCADE' })
  notes: Note[];

  @ManyToOne(() => Editor, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'editor_Id' })
  editor: Editor;

  @ManyToMany(() => Sticker, { onDelete: 'CASCADE' })
  @JoinTable({
    name: 'tbl_sticker_article',
    joinColumn: { name: 'articleId' },
    inverseJoinColumn: { name: 'stickerId' },
  })
  stickers: Sticker[];
}
