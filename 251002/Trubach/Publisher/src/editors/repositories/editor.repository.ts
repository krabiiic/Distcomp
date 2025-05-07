import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { Client } from 'pg'; 
import { Editor } from '../editor.entity';
import { IEditorRepository } from '../interfaces/editor.interface';

@Injectable()
export class DatabaseEditorRepository implements IEditorRepository {
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

  async create(editor: Editor): Promise<Editor> {
    const existingQuery = 'SELECT * FROM tbl_editor WHERE login = $1;';
    const existingValues = [editor.login];
    const existingRes = await this.client.query(existingQuery, existingValues);

    if ((existingRes?.rowCount ?? 0) > 0) {
      throw new HttpException('Login already exists', HttpStatus.FORBIDDEN);  
    }

    const query = `
      INSERT INTO tbl_editor (login, password, firstname, lastname)
      VALUES ($1, $2, $3, $4)
      RETURNING *;
    `;
    const values = [editor.login, editor.password, editor.firstname, editor.lastname];
    const res = await this.client.query(query, values);
    return res.rows[0];  
  }


  async findAll(): Promise<Editor[]> {
    const query = 'SELECT * FROM tbl_editor;';
    const res = await this.client.query(query);
    return res.rows;  
  }

  async findOne(id: number): Promise<Editor | null> {
    const query = 'SELECT * FROM tbl_editor WHERE id = $1;';
    const values = [id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null; 
  }

  async update(editor: Editor): Promise<Editor | null> {
    const query = `
      UPDATE tbl_editor
      SET login = $1, password = $2, firstname = $3, lastname = $4
      WHERE id = $5
      RETURNING *;
    `;
    const values = [editor.login, editor.password, editor.firstname, editor.lastname, editor.id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null;  
  }

  async delete(id: number): Promise<boolean> {
    const query = 'DELETE FROM tbl_editor WHERE id = $1 RETURNING *;';
    const values = [id];
    const res = await this.client.query(query, values);
    return (res?.rowCount ?? 0) > 0; 
  }

  async close() {
    await this.client.end();
  }
}