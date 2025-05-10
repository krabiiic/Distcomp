import { IsString, IsNotEmpty, Length } from 'class-validator';

export class UserRequestTo {
    @IsString()
    @IsNotEmpty()
    @Length(2, 64)
    login: string;

    @IsString()
    @IsNotEmpty()
    @Length(8, 128)
    password: string;

    @IsString()
    @IsNotEmpty()
    @Length(2, 64)
    firstname: string;

    @IsString()
    @IsNotEmpty()
    @Length(2, 64)
    lastname: string;
}

export class UserResponseTo {
    id: number;
    @Length(2, 64)
    login: string;
    firstname: string;
    lastname: string;
}