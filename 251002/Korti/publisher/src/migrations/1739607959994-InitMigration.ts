import { MigrationInterface, QueryRunner } from 'typeorm';

export class InitMigration1739539398800 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`CREATE TABLE tbl_editor(
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        login TEXT UNIQUE,
        password TEXT,
        firstname TEXT,
        lastname TEXT
    );`);

    await queryRunner.query(`CREATE TABLE tbl_article(
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        "editor_Id" BIGINT,
        title TEXT UNIQUE,
        content TEXT,
        created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY ("editor_Id") REFERENCES tbl_editor(id) 
            ON DELETE CASCADE
    );`);

    await queryRunner.query(`CREATE TABLE tbl_note(
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        "articleId" BIGINT,
        content TEXT,
        FOREIGN KEY ("articleId") REFERENCES tbl_article(id) 
            ON DELETE CASCADE
    );`);

    await queryRunner.query(`CREATE TABLE tbl_sticker(
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        name TEXT UNIQUE NOT NULL
    );`);

    await queryRunner.query(`
    CREATE TABLE tbl_article_stickers (
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        "articleId" BIGINT REFERENCES tbl_article(id) 
            ON DELETE CASCADE,
        "stickerId" BIGINT REFERENCES tbl_sticker(id),
        UNIQUE ("articleId", "stickerId")
    )
`);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`DROP TABLE IF EXISTS "tbl_note"`);
    await queryRunner.query(`DROP TABLE IF EXISTS tbl_article_stickers`);
    await queryRunner.query(`DROP TABLE IF EXISTS "tbl_article"`);
    await queryRunner.query(`DROP TABLE IF EXISTS "tbl_sticker"`);
    await queryRunner.query(`DROP TABLE IF EXISTS "tbl_editor"`);
  }
}
