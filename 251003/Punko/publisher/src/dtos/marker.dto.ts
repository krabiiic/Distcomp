import { Length } from 'class-validator';

export class MarkerRequestTo {
    @Length(2, 32)
    name: string;
}

export class MarkerResponseTo {
    id: number;

    @Length(2, 32)
    name: string;
}