CREATE TABLE creator (
    id BIGSERIAL PRIMARY KEY,
    login TEXT NOT NULL UNIQUE CHECK (char_length(login) BETWEEN 2 AND 64),
    password TEXT NOT NULL CHECK (char_length(password) BETWEEN 8 AND 124),
    first_name TEXT NOT NULL CHECK (char_length(first_name) BETWEEN 2 AND 64),
    last_name TEXT NOT NULL CHECK (char_length(last_name) BETWEEN 2 AND 64)
);

CREATE TABLE topic (
    id SERIAL PRIMARY KEY,
    creator_id BIGINT NOT NULL REFERENCES creator(id),
    title TEXT NOT NULL CHECK (char_length(title) BETWEEN 2 AND 64),
    content TEXT NOT NULL CHECK (char_length(content) BETWEEN 4 AND 2048),
    created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    modified TIMESTAMPTZ DEFAULT NULL
);

CREATE TABLE notice (
    id SERIAL PRIMARY KEY,
    topic_id BIGINT NOT NULL REFERENCES topic(id),
    content TEXT NOT NULL CHECK(char_length(content) BETWEEN 2 AND 2048)
);

CREATE TABLE marker (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE CHECK(char_length(name) BETWEEN 2 AND 32) NOT NULL
);