CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    login TEXT NOT NULL UNIQUE CHECK (char_length(login) BETWEEN 2 AND 64),
    password TEXT NOT NULL CHECK (char_length(password) BETWEEN 8 AND 124),
    firstname TEXT NOT NULL CHECK (char_length(firstname) BETWEEN 2 AND 64),
    lastname TEXT NOT NULL CHECK (char_length(lastname) BETWEEN 2 AND 64)
);

CREATE TABLE tweets (
    id SERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES authors(id),
    title TEXT NOT NULL CHECK (char_length(title) BETWEEN 2 AND 64),
    content TEXT NOT NULL CHECK (char_length(content) BETWEEN 4 AND 2048),
    created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    modified TIMESTAMPTZ DEFAULT NULL
);

CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    tweet_id BIGINT NOT NULL REFERENCES tweets(id),
    content TEXT NOT NULL CHECK(char_length(content) BETWEEN 2 AND 2048)
);

CREATE TABLE labels (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE CHECK(char_length(name) BETWEEN 2 AND 32) NOT NULL
);