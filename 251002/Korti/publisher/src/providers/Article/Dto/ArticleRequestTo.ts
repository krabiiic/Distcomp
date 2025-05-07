import {
  IsInt,
  IsString,
  MinLength,
  MaxLength,
  IsOptional,
  IsNumber,
} from 'class-validator';

export class ArticleRequestTo {
  @IsInt()
  editorId: number;

  @IsString()
  @MinLength(2)
  @MaxLength(64)
  title: string;

  @IsString()
  @MinLength(4)
  @MaxLength(2048)
  content: string;
}

export class UpdateArticleTo {
  @IsInt()
  id: number;

  @IsInt()
  editorId: number;

  @IsString()
  @MinLength(2)
  @MaxLength(64)
  title: string;

  @IsString()
  @MinLength(4)
  @MaxLength(2048)
  content: string;
}

export class ArticleSearchParamsDto {
  @IsOptional()
  @IsString({ each: true })
  stickerNames?: string[];

  @IsOptional()
  @IsNumber({}, { each: true })
  stickerIds?: number[];

  @IsOptional()
  @IsString()
  editorLogin?: string;

  @IsOptional()
  @IsString()
  title?: string;

  @IsOptional()
  @IsString()
  content?: string;
}
