import { Exclude, Expose } from 'class-transformer';
import { IsInt, IsString, Length } from 'class-validator';

@Exclude()
export class EditorResponseTo {
  @Expose()
  @IsInt()
  id: number;

  @IsString()
  @Length(2, 64)
  @Expose()
  login: string;

  @IsString()
  @Length(2, 64)
  @Expose()
  firstname: string;

  @IsString()
  @Length(2, 64)
  @Expose()
  lastname: string;
}
