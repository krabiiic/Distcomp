package marker

import (
	"context"
	"database/sql"
	"fmt"
	"log"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"lab3/publisher/internal/storage/model"
)

var (
	ErrNoticeNotFound   = fmt.Errorf("notice not found")
	ErrConstraintsCheck = fmt.Errorf("invalid data passed")
)

type instance struct {
	db *sqlx.DB
}

type Marker interface {
	CreateMarker(ctx context.Context, is model.Marker) (model.Marker, error)
	GetMarkers(ctx context.Context) ([]model.Marker, error)
	GetMarkerByID(ctx context.Context, id int64) (model.Marker, error)
	UpdateMarkerByID(ctx context.Context, is model.Marker) (model.Marker, error)
	DeleteMarkerByID(ctx context.Context, id int64) error
}

func New(db *sqlx.DB) Marker {
	return &instance{
		db: db,
	}
}

func (i *instance) CreateMarker(ctx context.Context, is model.Marker) (model.Marker, error) {
	query := `INSERT INTO tbl_marker (name) 
	          VALUES ($1) RETURNING id`

	var id int64

	err := i.db.QueryRowContext(ctx, query, is.Name).
		Scan(&id)
	if err != nil {
		log.Println(err)

		if pqErr, ok := err.(*pq.Error); ok && (pqErr.Code == "23505" || pqErr.Code == "23514") {
			return model.Marker{}, ErrConstraintsCheck
		}

		return model.Marker{}, fmt.Errorf("failed to create marker: %w", err)
	}

	is.ID = id

	return is, nil
}

func (i *instance) DeleteMarkerByID(ctx context.Context, id int64) error {
	query := `DELETE FROM tbl_marker WHERE id = $1`
	result, err := i.db.ExecContext(ctx, query, id)
	if err != nil {
		log.Println("Error executing DELETE query:", err)
		return fmt.Errorf("failed to delete marker: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		log.Println("Error getting rows affected:", err)
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		log.Println("No marker found with ID:", id)
		return ErrNoticeNotFound
	}

	return nil
}

func (i *instance) GetMarkerByID(ctx context.Context, id int64) (model.Marker, error) {
	var marker model.Marker
	query := `SELECT * FROM tbl_marker WHERE id = $1`

	err := i.db.GetContext(ctx, &marker, query, id)
	if err != nil {
		if err == sql.ErrNoRows {
			return marker, ErrNoticeNotFound
		}
		return marker, fmt.Errorf("failed to retrieve marker by ID: %w", err)
	}

	return marker, nil
}

func (i *instance) GetMarkers(ctx context.Context) ([]model.Marker, error) {
	var markers []model.Marker
	query := `SELECT * FROM tbl_marker`

	err := i.db.SelectContext(ctx, &markers, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve markers: %w", err)
	}

	if len(markers) == 0 {
		return []model.Marker{}, nil
	}

	return markers, nil
}

func (i *instance) UpdateMarkerByID(ctx context.Context, is model.Marker) (model.Marker, error) {
	query := `UPDATE tbl_marker SET name = $1
	          WHERE id = $2 RETURNING id, name`
	var updatedMarker model.Marker

	err := i.db.QueryRowContext(ctx, query, is.Name, is.ID).
		Scan(&updatedMarker.ID, &updatedMarker.Name)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("marker not found with id:", is.ID)
			return updatedMarker, ErrNoticeNotFound
		}

		log.Println("error with query:", err)
		return updatedMarker, fmt.Errorf("failed to update marker: %w", err)
	}

	return updatedMarker, nil
}
