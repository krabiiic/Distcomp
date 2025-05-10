import {Column, PrimaryGeneratedColumn} from 'typeorm'; // Используем для совместимости с DTO

export class Message {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ type: 'text' })
    content: string;


    @Column('int')
    articleid: number;
}