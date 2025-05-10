import {Column, PrimaryGeneratedColumn} from 'typeorm'; // Используем для совместимости с DTO

export class Reaction {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ type: 'text' })
    content: string;


    @Column('int')
    issueid: number;
}