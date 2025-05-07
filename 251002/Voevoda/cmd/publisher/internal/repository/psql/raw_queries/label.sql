-- name: GetAllLabels :many
SELECT * FROM tbl_label;

-- name: GetLabelByID :one
SELECT * FROM tbl_label WHERE id = $1;

-- name: CreateLabel :one
INSERT INTO tbl_label (name)
VALUES ($1)
RETURNING *;

-- name: DeleteLabel :exec
DELETE FROM tbl_label WHERE id = $1;

-- name: UpdateLabel :exec
UPDATE tbl_label
SET name = $2
WHERE id = $1; 