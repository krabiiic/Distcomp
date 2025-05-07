import {IsString, IsNotEmpty, Length} from 'class-validator';

export class ReactionRequestTo {
    @IsString()
    @IsNotEmpty()
    @Length(2,2048)
    content: string;

    id?: number;

    @IsNotEmpty()
    issueId: number;
}

export class ReactionResponseTo {
    id: number;

    @IsString()
    @IsNotEmpty()
    content: string;

    @IsNotEmpty()
    issueId: number;
}