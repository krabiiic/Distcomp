-- +goose Up
CREATE TABLE tbl_user (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(64) NOT NULL UNIQUE CHECK (LENGTH(login) > 2),
    password VARCHAR(128) NOT NULL CHECK (LENGTH(password) > 8),
    firstname VARCHAR(64) NOT NULL CHECK (LENGTH(firstname) > 2),
    lastname VARCHAR(64) NOT NULL CHECK (LENGTH(lastname) > 2)
);

-- +goose Down
DROP TABLE tbl_user; 