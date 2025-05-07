import { Editor } from '../editor.entity';

export interface IEditorRepository {
  create(editor: Editor): Promise<Editor>
  findAll(): Promise<Editor[]>;
  findOne(id: number): Promise<Editor | null>
  update(editor: Editor): Promise<Editor | null>
  delete(id: number): Promise<boolean>
}
