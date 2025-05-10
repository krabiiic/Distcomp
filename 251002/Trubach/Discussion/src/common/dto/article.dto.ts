export class ArticleRequestDto {
  readonly title: string;
  readonly content: string;
  readonly editorId: number;
}

export class ArticleResponseDto {
  readonly id: number;
  readonly title: string;
  readonly content: string;
  readonly editor_id: number;
  readonly created: number;
  readonly modified: number;
}
