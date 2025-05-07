export class NoteRequestDto {
    readonly id?: number;
    readonly content: string;
    readonly articleId: number;
}

export class NoteResponseDto {
    readonly id: number;
    readonly content: string;
    readonly articleId: number;
}