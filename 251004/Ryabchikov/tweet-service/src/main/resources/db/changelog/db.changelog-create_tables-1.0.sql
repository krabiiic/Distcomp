--liquibase formatted sql

--changeset ryabchikov:1
CREATE TABLE tbl_creator
(
    id        SERIAL PRIMARY KEY,
    login     VARCHAR(64) UNIQUE NOT NULL,
    password  VARCHAR(128)       NOT NULL,
    firstname VARCHAR(64)        NOT NULL,
    lastname  VARCHAR(64)        NOT NULL
);

CREATE TABLE tbl_mark
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE tbl_tweet
(
    id                 SERIAL PRIMARY KEY,
    creator_id         BIGINT        NOT NULL,
    title              VARCHAR(64)   NOT NULL,
    content            VARCHAR(2048) NOT NULL,
    created_time       DATE DEFAULT CURRENT_TIMESTAMP,
    last_modified_time DATE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_creator FOREIGN KEY (creator_id) REFERENCES tbl_creator (id) ON DELETE SET NULL
);

CREATE TABLE tbl_comment
(
    id       SERIAL PRIMARY KEY,
    tweet_id BIGINT        NOT NULL,
    content  VARCHAR(2048) NOT NULL,
    CONSTRAINT fk_tweet FOREIGN KEY (tweet_id) REFERENCES tbl_tweet (id) ON DELETE CASCADE
);