import {IsString, IsNotEmpty, Length} from 'class-validator';

export class MessageRequestTo {
    @IsString()
    @IsNotEmpty()
    @Length(2,2048)
    content: string;

    id?: number;

    @IsNotEmpty()
    articleId: number;
}

export class MessageResponseTo {
    id: number;

    @IsString()
    @IsNotEmpty()
    content: string;

    @IsNotEmpty()
    articleId: number;
}