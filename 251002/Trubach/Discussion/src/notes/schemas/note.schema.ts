// note.schema.ts
import { Schema, Document } from 'mongoose';

export interface NoteDocument extends Document {
  numericId: number;
  content: string;
  articleId: number;
}

export const NoteSchema = new Schema({
  _id: { type: Number, required: true, unique: true  }, 
  content: { type: String, required: true },
  articleId: { type: Number, required: true }
}, { _id: false }); 