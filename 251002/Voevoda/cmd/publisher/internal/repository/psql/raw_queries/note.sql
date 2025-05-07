-- name: GetAllNotes :many
SELECT * FROM tbl_note;

-- name: GetNoteByID :one
SELECT * FROM tbl_note WHERE id = $1;

-- name: CreateNote :one
INSERT INTO tbl_note (tweetId, content)
VALUES ($1, $2)
RETURNING *;

-- name: DeleteNote :exec
DELETE FROM tbl_note WHERE id = $1;

-- name: UpdateNote :exec
UPDATE tbl_note
SET tweetId = $2, content = $3
WHERE id = $1; 