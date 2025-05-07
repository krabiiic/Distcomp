-- name: GetAllTweets :many
SELECT * FROM tbl_tweet;

-- name: GetTweetByID :one
SELECT * FROM tbl_tweet WHERE id = $1;

-- name: CreateTweet :one
INSERT INTO tbl_tweet (user_id, title, content, created, modified)
VALUES ($1, $2, $3, $4, $5)
RETURNING *;

-- name: DeleteTweet :exec
DELETE FROM tbl_tweet WHERE id = $1;

-- name: UpdateTweet :exec
UPDATE tbl_tweet
SET user_id = $2, title = $3, content = $4, modified = $5
WHERE id = $1; 