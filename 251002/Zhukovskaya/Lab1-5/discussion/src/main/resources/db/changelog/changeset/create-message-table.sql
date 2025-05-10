USE distcomp;
CREATE TABLE IF NOT EXISTS tbl_message
(
    id bigint PRIMARY KEY,
    tweetId bigint,
    content text
);