import { drizzle } from 'drizzle-orm/node-postgres';
import { Client } from 'pg';

const client = new Client({
  connectionString: process.env.DATABASE_URL,
  password: 'postgres',
  user: 'postgres',
});

import { pgTable, serial, varchar, text, timestamp, integer } from 'drizzle-orm/pg-core';

export const tbl_editor = pgTable('tbl_editor', {
  id: serial('id').primaryKey(),
  login: varchar('login', { length: 64 }).notNull(),
  password: varchar('password', { length: 128 }).notNull(),
  firstname: varchar('firstname', { length: 64 }),
  lastname: varchar('lastname', { length: 64 }),
});

export const tbl_label = pgTable('tbl_label', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 50 }).notNull(),
});

export const tbl_article = pgTable('tbl_article', {
  id: serial('id').primaryKey(),
  title: varchar('title', { length: 200 }).notNull(),
  content: text('content').notNull(),
  editorId: integer('editor_id').references(() => tbl_editor.id),
  created: timestamp('created').defaultNow(),
  modified: timestamp('modified').defaultNow(),
});

export const tbl_note = pgTable('tbl_note', {
  id: serial('id').primaryKey(),
  content: text('content').notNull(),
  articleId: integer('articleId').references(() => tbl_article.id),
});


async function createDatabaseAndTables() {
  try {
    await client.connect();
    console.log('Connected to PostgreSQL');

    const dbName = 'distcomp';
    const res = await client.query(
      `SELECT 1 FROM pg_database WHERE datname = $1`,
      [dbName]
    );

    if (res.rowCount === 0) {
      await client.query(`CREATE DATABASE ${dbName}`);
      console.log(`Database "${dbName}" created`);
    } else {
      console.log(`Database "${dbName}" already exists`);
    }

    await client.end();

    const newClient = new Client({
      connectionString: process.env.DATABASE_URL,
      database: 'distcomp',
      password: 'postgres',
      user: 'postgres',
    });

    await newClient.connect();
    console.log('Connected to "distcomp" database');

    const db = drizzle(newClient);

    await db.execute(`
      CREATE TABLE IF NOT EXISTS tbl_editor (
        id SERIAL PRIMARY KEY,
        login VARCHAR(64) NOT NULL,
        password VARCHAR(128) NOT NULL,
        firstname VARCHAR(64),
        lastname VARCHAR(64)
      );

      CREATE TABLE IF NOT EXISTS tbl_label (
        id SERIAL PRIMARY KEY,
        name VARCHAR(32) NOT NULL
      );

      CREATE TABLE IF NOT EXISTS tbl_article (
        id SERIAL PRIMARY KEY,
        title VARCHAR(64) NOT NULL,
        content TEXT NOT NULL,
        editor_id INTEGER REFERENCES tbl_editor(id),
        created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );

      CREATE TABLE IF NOT EXISTS tbl_note (
        id SERIAL PRIMARY KEY,
        content TEXT NOT NULL,
        articleId INTEGER REFERENCES tbl_article(id) ON DELETE CASCADE
      );
    `);

    console.log('Tables created successfully');
    
    await newClient.end();
  } catch (error) {
    console.error('Error executing query', error.stack);
  }
}
// createDatabaseAndTables()

 
