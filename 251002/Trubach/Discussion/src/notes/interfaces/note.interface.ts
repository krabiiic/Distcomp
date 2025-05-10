import { Note } from '../note.entity';

export interface INoteRepository {
    create(note: Note): Promise<Note>;
    findAll(): Promise<Note[]>;
    findOne(id: number): Promise<Note | null>;
    update(note: Note): Promise<Note | null>;
    delete(id: number): Promise<boolean>;
}