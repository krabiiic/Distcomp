-- +goose Up
CREATE TABLE tbl_label (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL CHECK (LENGTH(name) > 2)
);

-- +goose Down
DROP TABLE tbl_label; 