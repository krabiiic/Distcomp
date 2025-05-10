import { Length } from 'class-validator';

export class IssueRequestTo {
    @Length(2, 64)
    title: string;
    id?: number;
    markers?: string[];
    @Length(4, 2048)
    content: string;
    authorId: number;
}

export class IssueResponseTo {
    id: number;

    @Length(2, 64)
    title: string;

    content: string;
    authorId: number;
    created: number;
    modified: number;
}