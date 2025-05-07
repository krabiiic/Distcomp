import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { Client } from 'pg'; 
import { Article } from '../article.entity';
import { IArticleRepository } from '../interfaces/article.interface';
import { ArticleRequestDto } from 'src/common/dto/article.dto';

@Injectable()
export class DatabaseArticleRepository implements IArticleRepository {
  private client: Client;

  constructor() {
    this.client = new Client({
      port: 5432,
      database: 'distcomp',
      password: 'postgres',
      user: 'postgres',
    });
    this.client.connect();  
  }

  // @ts-ignore
  async create(articleRequestDto: ArticleRequestDto): Promise<Article> {
    const editorCheckQuery = 'SELECT * FROM tbl_editor WHERE id = $1;';
    const editorCheckValues = [articleRequestDto.editorId];
    const editorCheckRes = await this.client.query(editorCheckQuery, editorCheckValues);
  
    if (editorCheckRes.rowCount === 0) {
      throw new HttpException('Editor not found', HttpStatus.BAD_REQUEST);
    }
  
    const titleCheckQuery = 'SELECT * FROM tbl_article WHERE title = $1;';
    const titleCheckValues = [articleRequestDto.title];
    const titleCheckRes = await this.client.query(titleCheckQuery, titleCheckValues);
  
    if ((titleCheckRes?.rowCount ?? 0)  > 0) {
      throw new HttpException('Article title must be unique', HttpStatus.FORBIDDEN);
    }
  
    const query = `
      INSERT INTO tbl_article (title, content, editor_id)   
      VALUES ($1, $2, $3)
      RETURNING *;
    `;
    const values = [articleRequestDto.title, articleRequestDto.content, articleRequestDto.editorId];
    const res = await this.client.query(query, values);
    return res.rows[0]; 
  }

  async findAll(): Promise<Article[]> {
    const query = 'SELECT * FROM tbl_article;';
    const res = await this.client.query(query);
    return res.rows; 
  }

  async findOne(id: number): Promise<Article | null> {
    const query = 'SELECT * FROM tbl_article WHERE id = $1;';
    const values = [id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null;  
  }

  async update(id: number, article: Article): Promise<Article | null> {
    const query = `
      UPDATE tbl_article
      SET title = $1, content = $2
      WHERE id = $3
      RETURNING *;
    `;
    const values = [article.title, article.content, id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null;  
  }

  async delete(id: number): Promise<boolean> {
    const query = 'DELETE FROM tbl_article WHERE id = $1 RETURNING *;';
    const values = [id];
    const res = await this.client.query(query, values);
    return (res?.rowCount ?? 0) > 0;  
  }

  async close() {
    await this.client.end();
  }
}