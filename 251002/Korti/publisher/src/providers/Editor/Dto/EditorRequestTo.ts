import { IsInt, IsString, Length } from 'class-validator';

export class EditorRequestTo {
  @IsString()
  @Length(2, 64)
  login: string;

  @IsString()
  @Length(8, 128)
  password: string;

  @IsString()
  @Length(2, 64)
  firstname: string;

  @IsString()
  @Length(2, 64)
  lastname: string;
}

export class UpdateEditorDto {
  @IsInt()
  id: number;

  @IsString()
  @Length(2, 64)
  login: string;

  @IsString()
  @Length(8, 128)
  password: string;

  @IsString()
  @Length(2, 64)
  firstname: string;

  @IsString()
  @Length(2, 64)
  lastname: string;
}
