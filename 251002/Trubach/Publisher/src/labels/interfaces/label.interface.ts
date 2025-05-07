import { Label } from '../label.entity';

export interface ILabelRepository {
    create(label: Label): Promise<Label>;
    findAll(): Promise<Label[]>;
    findOne(id: number): Promise<Label | null>;
    update(label: Label): Promise<Label | null>;
    delete(id: number): Promise<boolean>;
}