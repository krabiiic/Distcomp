export class ReactionRequestTo {
    id?: number;
    issueId: number;
    content: string;
}

export class ReactionResponseTo {
    id: number;
    content: string;
    issueId: number;
}