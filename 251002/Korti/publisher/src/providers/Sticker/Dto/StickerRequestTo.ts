import { IsInt, IsString, MaxLength, MinLength } from 'class-validator';

export class StickerRequestTo {
  @IsString()
  @MinLength(2)
  @MaxLength(32)
  name: string;
}

export class UpdateStickerTo {
  @IsInt()
  id: number;

  @IsString()
  @MinLength(2)
  @MaxLength(32)
  name: string;
}
