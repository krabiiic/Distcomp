using System;
using FluentMigrator;

namespace Core.Migrations;

[Migration(20250216101011, TransactionBehavior.None)]
public class InitSchema : Migration
{
    public override void Up()
    {
        const string init_script =
"""
CREATE ROLE root WITH LOGIN PASSWORD 'root';
CREATE TABLE IF NOT EXISTS tbl_creator (
  id        bigserial     NOT NULL,
  login     varchar(64)   NOT NULL UNIQUE CHECK (LENGTH(login) >= 2),
  password  varchar(128)  NOT NULL CHECK (LENGTH(password) >= 8),
  firstname varchar(64)   NOT NULL CHECK (LENGTH(firstname) >= 2),
  lastname  varchar(64)   NOT NULL CHECK (LENGTH(lastname) >= 2),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tbl_article (
  id        bigserial   NOT NULL,
  creator_id  bigint      NOT NULL, 
  title     varchar(64) NOT NULL UNIQUE CHECK (LENGTH(title) >= 2),
  content   text        NOT NULL CHECK (LENGTH(content) >= 4),
  created   timestamp   NOT NULL DEFAULT NOW(),
  modified  timestamp   NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (creator_id) REFERENCES tbl_creator (id)
);

CREATE TABLE IF NOT EXISTS tbl_message (
  id        bigserial   NOT NULL,
  article_id    bigint      NOT NULL,
  content   text        NOT NULL CHECK(LENGTH(content) >= 2),
  PRIMARY KEY (id),
  FOREIGN KEY (article_id) REFERENCES tbl_article (id)
);

CREATE TABLE IF NOT EXISTS tbl_tag (
  id        bigserial   NOT NULL,
  name      varchar(32) NOT NULL UNIQUE CHECK(LENGTH(name) >= 2),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS m2m_article_tag (
  tag_id  bigint NOT NULL,
  article_id bigint NOT NULL,
  PRIMARY KEY (article_id, tag_id),
  FOREIGN KEY (article_id) REFERENCES tbl_article (id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tbl_tag (id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION delete_orphaned_tags()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверяем, остались ли записи с таким tag_id в m2m_article_tag
    IF NOT EXISTS (
        SELECT 1
        FROM m2m_article_tag
        WHERE tag_id = OLD.tag_id
    ) THEN
        -- Если записей нет, удаляем запись из tbl_tag
        DELETE FROM tbl_tag
        WHERE id = OLD.tag_id;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_delete_orphaned_tags
AFTER DELETE ON m2m_article_tag
FOR EACH ROW
EXECUTE FUNCTION delete_orphaned_tags();
""";
        Execute.Sql(init_script);
    }

    public override void Down()
    {
        const string init_down_script = 
"""
DROP TABLE IF EXISTS m2m_article_tag;
DROP TABLE IF EXISTS tbl_tag;
DROP TABLE IF EXISTS tbl_message;
DROP TABLE IF EXISTS tbl_article;
DROP TABLE IF EXISTS tbl_creator;
""";
        Execute.Sql(init_down_script);
    }
}
