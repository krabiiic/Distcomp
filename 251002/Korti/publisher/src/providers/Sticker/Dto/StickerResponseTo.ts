import { Exclude, Expose } from 'class-transformer';
import { IsInt, IsString, MaxLength, MinLength } from 'class-validator';
@Exclude()
export class StickerResponseTo {
  @IsInt()
  @Expose()
  id: number;

  @IsString()
  @Expose()
  @MinLength(2)
  @MaxLength(32)
  name: string;
}
