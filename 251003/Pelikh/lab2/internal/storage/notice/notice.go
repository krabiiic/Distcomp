package notice

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"lab2/internal/model"
	"log"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
)

var (
	ErrNoticeNotFound    = fmt.Errorf("Repo not found")
	ErrFailedToUpdate    = fmt.Errorf("failed to update Repo")
	ErrFailedToDelete    = fmt.Errorf("failed to delete Repo")
	ErrInvalidForeignKey = fmt.Errorf("invalid foreign key passed")
)

type repo struct {
	db *sqlx.DB
}

type Repo interface {
	Create(ctx context.Context, req model.Notice) (model.Notice, error)
	GetList(ctx context.Context) ([]model.Notice, error)
	Get(ctx context.Context, id int64) (model.Notice, error)
	Update(ctx context.Context, req model.Notice) (model.Notice, error)
	Delete(ctx context.Context, id int64) error
}

func New(db *sqlx.DB) Repo {
	return repo{
		db: db,
	}
}

func (r repo) Create(ctx context.Context, req model.Notice) (model.Notice, error) {
	query := `INSERT INTO tbl_notice (topic_id, content) 
	          VALUES ($1, $2) RETURNING id`

	var id int64

	err := r.db.QueryRowContext(ctx, query, req.TopicID, req.Content).Scan(&id)
	if err != nil {
		log.Println(err)
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23503" {
			return model.Notice{}, ErrInvalidForeignKey
		}

		return model.Notice{}, fmt.Errorf("failed to create notice: %w", err)
	}

	req.ID = id

	return req, nil
}

func (r repo) Delete(ctx context.Context, id int64) error {
	query := `DELETE FROM tbl_notice WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete Repo: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return ErrNoticeNotFound
	}

	return nil
}

func (r repo) Get(ctx context.Context, id int64) (model.Notice, error) {
	var result model.Notice

	query := `SELECT * FROM tbl_notice WHERE id = $1`

	err := r.db.GetContext(ctx, &result, query, id)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrNoticeNotFound
		}

		return result, fmt.Errorf("failed to retrieve Repo by ID: %w", err)
	}

	return result, nil
}

func (r repo) GetList(ctx context.Context) ([]model.Notice, error) {
	var result []model.Notice

	query := `SELECT * FROM tbl_notice`

	err := r.db.SelectContext(ctx, &result, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve result: %w", err)
	}

	if len(result) == 0 {
		return []model.Notice{}, nil
	}

	return result, nil
}

func (r repo) Update(ctx context.Context, req model.Notice) (model.Notice, error) {
	query := `UPDATE tbl_notice SET topic_id = $1, content = $2
	          WHERE id = $3 RETURNING id, topic_id, content`

	var result model.Notice

	err := r.db.QueryRowContext(ctx, query, req.TopicID, req.Content, req.ID).
		Scan(&result.ID, &result.TopicID, &result.Content)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrNoticeNotFound
		}

		return result, fmt.Errorf("failed to update Repo: %w", err)
	}

	return result, nil
}
