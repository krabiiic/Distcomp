export class MessageRequestTo {
    id?: number;
    articleId: number;
    content: string;
}

export class MessageResponseTo {
    id: number;
    content: string;
    articleId: number;
}