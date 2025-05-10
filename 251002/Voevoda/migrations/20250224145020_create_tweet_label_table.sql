-- +goose Up
CREATE TABLE tbl_tweet_label (
    id BIGSERIAL PRIMARY KEY,
    tweetId BIGINT REFERENCES tbl_tweet(id),
    labelId BIGINT REFERENCES tbl_label(id)
);

-- +goose Down
DROP TABLE tbl_tweet_label; 