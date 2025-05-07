import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { Client } from 'pg'; 
import { Label } from '../label.entity';
import { ILabelRepository } from '../interfaces/label.interface';

@Injectable()
export class DatabaseLabelRepository implements ILabelRepository {
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

  async create(label: Label): Promise<Label> {
    const existingQuery = 'SELECT * FROM tbl_label WHERE name = $1;';
    const existingValues = [label.name];
    const existingRes = await this.client.query(existingQuery, existingValues);

    if ((existingRes?.rowCount ?? 0) > 0) {
      throw new HttpException('Label with this name already exists', HttpStatus.FORBIDDEN);  
    }

    const query = `
      INSERT INTO tbl_label (name)
      VALUES ($1)
      RETURNING *;
    `;
    const values = [label.name];
    const res = await this.client.query(query, values);
    return res.rows[0]; 
  }

  async findAll(): Promise<Label[]> {
    const query = 'SELECT * FROM tbl_label;';
    const res = await this.client.query(query);
    return res.rows;  
  }

  async findOne(id: number): Promise<Label | null> {
    const query = 'SELECT * FROM tbl_label WHERE id = $1;';
    const values = [id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null; 
  }

  async update(label: Label): Promise<Label | null> {
    const query = `
      UPDATE tbl_label
      SET name = $1
      WHERE id = $2
      RETURNING *;
    `;
    const values = [label.name, label.id];
    const res = await this.client.query(query, values);
    return res.rows[0] || null;  
  }

  async delete(id: number): Promise<boolean> {
    const query = 'DELETE FROM tbl_label WHERE id = $1 RETURNING *;';
    const values = [id];
    const res = await this.client.query(query, values);
    return (res?.rowCount ?? 0) > 0; 
  }

  async close() {
    await this.client.end();
  }
}