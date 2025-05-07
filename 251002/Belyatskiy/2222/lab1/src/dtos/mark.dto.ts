import { Length } from 'class-validator';

export class MarkRequestTo {
    @Length(2, 32)
    name: string;
}

export class MarkResponseTo {
    id: number;

    @Length(2, 32)
    name: string;
}