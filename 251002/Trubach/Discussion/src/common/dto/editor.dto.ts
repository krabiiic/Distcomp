export class EditorRequestDto {
  readonly login: string;
  readonly password: string;
  readonly firstname: string;
  readonly lastname: string;
}

export class EditorResponseDto {
  readonly id: number;
  readonly login: string;
  readonly password: string;
  readonly firstname: string;
  readonly lastname: string;
}
