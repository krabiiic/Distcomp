import { DataSource } from 'typeorm';
import 'reflect-metadata';
import { Editor } from './entities/Editor';
import { Article } from './entities/Article';
import { Note } from './entities/Note';
import { Sticker } from './entities/Sticker';
import { config } from 'dotenv';
config();

const AppDataSource = new DataSource({
  type: 'postgres',
  host: process.env.DB_HOST,
  port: Number(process.env.DB_PORT),
  username: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  entities: [Editor, Article, Note, Sticker],
  //migrations: ['src/migrations/**/*.ts'],
  migrations: [
    'dist/migrations/*.js', // Important: Use .js, not .ts
  ],
  migrationsTableName: 'test-migration',
  logging: true,
});

export default AppDataSource;
