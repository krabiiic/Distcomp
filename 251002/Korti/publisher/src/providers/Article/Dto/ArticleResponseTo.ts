import { IsDate, IsInt, IsString, MaxLength, MinLength } from 'class-validator';
import { Exclude, Expose } from 'class-transformer';
@Exclude()
export class ArticleResponseTo {
  @Expose()
  @IsInt()
  id: number;

  @Expose()
  @IsInt()
  editorId: number;

  @Expose()
  @IsString()
  @MinLength(2)
  @MaxLength(64)
  title: string;

  @Expose()
  @IsString()
  @MinLength(4)
  @MaxLength(2048)
  content: string;

  @Expose()
  @IsDate()
  created: Date;

  @Expose()
  modified: Date | null;
}
