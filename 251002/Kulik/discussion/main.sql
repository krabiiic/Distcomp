--drop table creator CASCADE;
drop table tbl_creator cascade;
drop table tbl_story cascade;
--drop table story cascade;
drop table tbl_mark cascade;
--drop table mark cascade;
drop table tbl_note cascade;


CREATE TABLE tbl_creator (
    id SERIAL PRIMARY KEY,
    login VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255)
);

CREATE TABLE tbl_mark (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE tbl_story (
    id SERIAL PRIMARY KEY,
    creator_id INT NOT NULL REFERENCES tbl_creator(id) ON DELETE CASCADE,
    title VARCHAR(255) UNIQUE,
    content TEXT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_note (
    id SERIAL PRIMARY KEY,
    story_id INT NOT NULL REFERENCES tbl_story(id) ON DELETE CASCADE,
    content TEXT NOT NULL
);


