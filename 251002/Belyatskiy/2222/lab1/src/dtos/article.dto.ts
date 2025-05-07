import { Length } from 'class-validator';

export class ArticleRequestTo {
    @Length(2, 64)
    title: string;
    id?: number;
    marks?: string[];
    @Length(4, 2048)
    content: string;
    userId: number;
}

export class ArticleResponseTo {
    id: number;

    @Length(2, 64)
    title: string;

    content: string;
    userId: number;
    created: number;
    modified: number;
}