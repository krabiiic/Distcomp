-- +goose Up
CREATE TABLE tbl_note (
    id BIGSERIAL PRIMARY KEY,
    tweetId BIGINT REFERENCES tbl_tweet(id),
    content VARCHAR(2048) NOT NULL CHECK (LENGTH(content) > 2)
);

-- +goose Down
DROP TABLE tbl_note; 