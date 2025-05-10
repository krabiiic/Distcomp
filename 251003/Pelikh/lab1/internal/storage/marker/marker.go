package marker

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"lab1/internal/model"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
)

var (
	ErrMarkerNotFound   = fmt.Errorf("marker not found")
	ErrConstraintsCheck = fmt.Errorf("invalid data passed")
)

type repo struct {
	db *sqlx.DB
}

type Repo interface {
	Create(ctx context.Context, req model.Marker) (model.Marker, error)
	GetList(ctx context.Context) ([]model.Marker, error)
	Get(ctx context.Context, id int64) (model.Marker, error)
	Update(ctx context.Context, req model.Marker) (model.Marker, error)
	Delete(ctx context.Context, id int64) error
}

func New(db *sqlx.DB) Repo {
	return repo{
		db: db,
	}
}

func (r repo) Create(ctx context.Context, req model.Marker) (model.Marker, error) {
	query := `INSERT INTO marker (name) 
	          VALUES ($1) RETURNING id`

	var id int64

	err := r.db.QueryRowContext(ctx, query, req.Name).Scan(&id)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return model.Marker{}, ErrConstraintsCheck
		}

		return model.Marker{}, fmt.Errorf("failed to create marker: %w", err)
	}

	req.ID = id

	return req, nil
}

func (r repo) Delete(ctx context.Context, id int64) error {
	query := `DELETE FROM marker WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete marker: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return ErrMarkerNotFound
	}

	return nil
}

func (r repo) Get(ctx context.Context, id int64) (model.Marker, error) {
	var result model.Marker

	query := `SELECT * FROM marker WHERE id = $1`

	err := r.db.GetContext(ctx, &result, query, id)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrMarkerNotFound
		}
		return result, fmt.Errorf("failed to retrieve marker by ID: %w", err)
	}

	return result, nil
}

func (r repo) GetList(ctx context.Context) ([]model.Marker, error) {
	var result []model.Marker

	query := `SELECT * FROM marker`

	err := r.db.SelectContext(ctx, &result, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve result: %w", err)
	}

	if len(result) == 0 {
		return []model.Marker{}, nil
	}

	return result, nil
}

func (r repo) Update(ctx context.Context, req model.Marker) (model.Marker, error) {
	query := `UPDATE marker SET name = $1
	          WHERE id = $2 RETURNING id, name`

	var result model.Marker

	err := r.db.QueryRowContext(ctx, query, req.Name, req.ID).Scan(&result.ID, &result.Name)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrMarkerNotFound
		}

		return result, fmt.Errorf("failed to update marker: %w", err)
	}

	return result, nil
}
