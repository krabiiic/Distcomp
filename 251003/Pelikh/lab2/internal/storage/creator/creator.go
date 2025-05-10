package creator

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"lab2/internal/model"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
)

var (
	ErrLoginExists     = fmt.Errorf("creator with this login already exists")
	ErrCreatorNotFound = fmt.Errorf("creator not found")
)

type Repo interface {
	Create(ctx context.Context, req model.Creator) (model.Creator, error)
	GetList(ctx context.Context) ([]model.Creator, error)
	Get(ctx context.Context, id int64) (model.Creator, error)
	Update(ctx context.Context, req model.Creator) (model.Creator, error)
	Delete(ctx context.Context, id int64) error
}

type repo struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) Repo {
	return repo{
		db: db,
	}
}

func (r repo) Create(ctx context.Context, req model.Creator) (model.Creator, error) {
	query := `INSERT INTO tbl_creator (login, password, firstname, lastname) 
	          VALUES ($1, $2, $3, $4) RETURNING id`

	var id int64

	err := r.db.QueryRowContext(ctx, query, req.Login, req.Password, req.FirstName, req.LastName).Scan(&id)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return model.Creator{}, ErrLoginExists
		}

		return model.Creator{}, fmt.Errorf("failed to create creator: %w", err)
	}

	req.ID = id

	return req, nil
}

func (r repo) GetList(ctx context.Context) ([]model.Creator, error) {
	var result []model.Creator

	query := `SELECT * FROM tbl_creator`

	err := r.db.SelectContext(ctx, &result, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve result: %w", err)
	}

	if len(result) == 0 {
		return []model.Creator{}, nil
	}

	return result, nil
}

func (r repo) Get(ctx context.Context, id int64) (model.Creator, error) {
	var result model.Creator

	query := `SELECT id, login, password, firstname, lastname FROM tbl_creator WHERE id = $1`

	err := r.db.GetContext(ctx, &result, query, id)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrCreatorNotFound
		}

		return result, fmt.Errorf("failed to retrieve Repo by ID: %w", err)
	}

	return result, nil
}

func (r repo) Update(ctx context.Context, req model.Creator) (model.Creator, error) {
	var result model.Creator

	query := `UPDATE tbl_creator SET login = $1, password = $2, firstname = $3, lastname = $4 
              WHERE id = $5 RETURNING id, login, password, firstname, lastname`

	err := r.db.QueryRowContext(ctx, query, req.Login, req.Password, req.FirstName, req.LastName, req.ID).
		Scan(&result.ID, &result.Login, &result.Password, &result.FirstName, &result.LastName)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrCreatorNotFound
		}

		return result, fmt.Errorf("failed to update Repo: %w", err)
	}

	return result, nil
}

func (r repo) Delete(ctx context.Context, id int64) error {
	query := `DELETE FROM tbl_creator WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete Repo: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return ErrCreatorNotFound
	}

	return nil
}
