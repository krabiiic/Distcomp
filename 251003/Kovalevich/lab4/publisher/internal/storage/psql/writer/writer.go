package Writer

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
	ErrLoginExists       = fmt.Errorf("user with this login already exists")
	ErrWriterNotFound    = fmt.Errorf("Writer not found")
	ErrFailedToCreate    = fmt.Errorf("failed to create Writer")
	ErrFailedToUpdate    = fmt.Errorf("failed to update Writer")
	ErrFailedToDelete    = fmt.Errorf("failed to delete Writer")
	ErrInvalidWriterData = fmt.Errorf("invalid Writer data")
)

type Writer interface {
	CreateWriter(ctx context.Context, cr model.Writer) (model.Writer, error)
	GetWriters(ctx context.Context) ([]model.Writer, error)
	GetWriterByID(ctx context.Context, id int64) (model.Writer, error)
	UpdateWriterByID(ctx context.Context, cr model.Writer) (model.Writer, error)
	DeleteWriterByID(ctx context.Context, id int64) error
}

type instance struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) Writer {
	return &instance{
		db: db,
	}
}

func (i *instance) CreateWriter(ctx context.Context, cr model.Writer) (model.Writer, error) {
	query := `INSERT INTO tbl_Writer (login, password, firstname, lastname) 
	          VALUES ($1, $2, $3, $4) RETURNING id`

	var id int64
	err := i.db.QueryRowContext(ctx, query, cr.Login, cr.Password, cr.FirstName, cr.LastName).Scan(&id)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return model.Writer{}, ErrLoginExists
		}
		return model.Writer{}, fmt.Errorf("failed to create Writer: %w", err)
	}

	cr.ID = id

	return cr, nil
}

func (i *instance) GetWriters(ctx context.Context) ([]model.Writer, error) {
	var Writers []model.Writer
	query := `SELECT * FROM tbl_Writer`

	err := i.db.SelectContext(ctx, &Writers, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve Writers: %w", err)
	}

	if len(Writers) == 0 {
		return []model.Writer{}, nil
	}

	return Writers, nil
}

func (i *instance) GetWriterByID(ctx context.Context, id int64) (model.Writer, error) {
	var Writer model.Writer
	query := `SELECT id, login, password, firstname, lastname FROM tbl_Writer WHERE id = $1`

	err := i.db.GetContext(ctx, &Writer, query, id)
	if err != nil {
		if err == sql.ErrNoRows {
			return Writer, ErrWriterNotFound
		}
		return Writer, fmt.Errorf("failed to retrieve Writer by ID: %w", err)
	}

	return Writer, nil
}

func (i *instance) UpdateWriterByID(ctx context.Context, cr model.Writer) (model.Writer, error) {
	query := `UPDATE tbl_Writer SET login = $1, password = $2, firstname = $3, lastname = $4 WHERE id = $5 RETURNING id, login, password, firstname, lastname`
	var updatedWriter model.Writer

	err := i.db.QueryRowContext(ctx, query, cr.Login, cr.Password, cr.FirstName, cr.LastName, cr.ID).
		Scan(&updatedWriter.ID, &updatedWriter.Login, &updatedWriter.Password, &updatedWriter.FirstName, &updatedWriter.LastName)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("Writer not found with id:", cr.ID)
			return updatedWriter, ErrWriterNotFound
		}

		log.Println("error with query:", err)
		return updatedWriter, fmt.Errorf("failed to update Writer: %w", err)
	}

	return updatedWriter, nil
}

func (i *instance) DeleteWriterByID(ctx context.Context, id int64) error {
	query := `DELETE FROM tbl_Writer WHERE id = $1`
	result, err := i.db.ExecContext(ctx, query, id)
	if err != nil {
		log.Println("Error executing DELETE query:", err)
		return fmt.Errorf("failed to delete Writer: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		log.Println("Error getting rows affected:", err)
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		log.Println("No Writer found with ID:", id)
		return ErrWriterNotFound
	}

	return nil
}
