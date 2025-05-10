-- name: GetAllUsers :many
SELECT * FROM tbl_user;

-- name: GetUserByID :one
SELECT * FROM tbl_user WHERE id = $1;

-- name: CreateUser :one
INSERT INTO tbl_user (login, password, firstname, lastname)
VALUES ($1, $2, $3, $4)
RETURNING *;

-- name: DeleteUser :exec
DELETE FROM tbl_user WHERE id = $1;

-- name: UpdateUser :exec
UPDATE tbl_user
SET login = $2, password = $3, firstname = $4, lastname = $5
WHERE id = $1; 