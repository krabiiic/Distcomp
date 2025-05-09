package topic

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"time"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"lab3/publisher/internal/storage/model"
)

var (
	ErrTopicNotFound     = fmt.Errorf("Topic not found")
	ErrFailedToCreate    = fmt.Errorf("failed to create Topic")
	ErrFailedToUpdate    = fmt.Errorf("failed to update Topic")
	ErrFailedToDelete    = fmt.Errorf("failed to delete Topic")
	ErrInvalidTopicData  = fmt.Errorf("invalid Topic data")
	ErrInvalidForeignKey = fmt.Errorf("invalid foreign key passed")
)

type Topic interface {
	CreateTopic(ctx context.Context, is model.Topic) (model.Topic, error)
	GetTopics(ctx context.Context) ([]model.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (model.Topic, error)
	UpdateTopicByID(ctx context.Context, is model.Topic) (model.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

type instance struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) Topic {
	return &instance{
		db: db,
	}
}

func (i *instance) CreateTopic(ctx context.Context, is model.Topic) (model.Topic, error) {
	tx, err := i.db.BeginTxx(ctx, nil)
	if err != nil {
		return model.Topic{}, fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer func() {
		if err != nil {
			tx.Rollback()
		}
	}()

	topicQuery := `INSERT INTO tbl_topic (creator_id, title, content) 
                   VALUES ($1, $2, $3) RETURNING id, created`

	var id int64
	var created time.Time

	err = tx.QueryRowxContext(ctx, topicQuery, is.CreatorID, is.Title, is.Content).
		Scan(&id, &created)
	if err != nil {
		log.Println("CreateTopic error:", err)

		if pqErr, ok := err.(*pq.Error); ok {
			switch pqErr.Code {
			case "23503":
				return model.Topic{}, ErrInvalidForeignKey
			case "23505":
				return model.Topic{}, ErrInvalidTopicData
			case "23502":
				return model.Topic{}, ErrInvalidTopicData
			}
		}
		return model.Topic{}, fmt.Errorf("%w: %v", ErrFailedToCreate, err)
	}

	is.ID = id
	is.Created = created

	if is.Markers != nil {
		markerQuery := `INSERT INTO tbl_marker (name) VALUES ($1) RETURNING id`
		markerTopicQuery := `INSERT INTO tbl_topic_marker (topic_id, marker_id) VALUES ($1, $2)`

		for _, marker := range is.Markers {
			var markerID int64

			err = tx.QueryRowxContext(ctx, markerQuery, marker).Scan(&markerID)
			if err != nil {
				return model.Topic{}, fmt.Errorf("failed to create marker: %w", err)
			}

			_, err = tx.ExecContext(ctx, markerTopicQuery, id, markerID)
			if err != nil {
				return model.Topic{}, fmt.Errorf("failed to link marker to Topic: %w", err)
			}
		}
	}

	if err = tx.Commit(); err != nil {
		return model.Topic{}, fmt.Errorf("failed to commit transaction: %w", err)
	}

	return is, nil
}

func (i *instance) GetTopics(ctx context.Context) ([]model.Topic, error) {
	var topics []model.Topic
	query := `SELECT * FROM tbl_topic`

	err := i.db.SelectContext(ctx, &topics, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve Topics: %w", err)
	}

	if len(topics) == 0 {
		return []model.Topic{}, nil
	}

	return topics, nil
}

func (i *instance) GetTopicByID(ctx context.Context, id int64) (model.Topic, error) {
	var topic model.Topic
	query := `SELECT id, creator_id, title, content, created, modified FROM tbl_topic WHERE id = $1`

	err := i.db.GetContext(ctx, &topic, query, id)
	if err != nil {
		if err == sql.ErrNoRows {
			return topic, ErrTopicNotFound
		}
		return topic, fmt.Errorf("failed to retrieve Topic by ID: %w", err)
	}

	return topic, nil
}

func (i *instance) UpdateTopicByID(ctx context.Context, is model.Topic) (model.Topic, error) {
	query := `UPDATE tbl_topic SET creator_id = $1, title = $2, content = $3, modified = $4 
	          WHERE id = $5 RETURNING id, creator_id, title, content, created, modified`
	var updatedTopic model.Topic

	err := i.db.QueryRowContext(ctx, query, is.CreatorID, is.Title, is.Content, is.Modified, is.ID).
		Scan(&updatedTopic.ID, &updatedTopic.CreatorID, &updatedTopic.Title, &updatedTopic.Content, &updatedTopic.Created, &updatedTopic.Modified)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("Topic not found with id:", is.ID)
			return updatedTopic, ErrTopicNotFound
		}

		log.Println("error with query:", err)
		return updatedTopic, fmt.Errorf("failed to update Topic: %w", err)
	}

	return updatedTopic, nil
}

func (i *instance) DeleteTopicByID(ctx context.Context, id int64) error {
	query := `DELETE FROM tbl_topic WHERE id = $1`
	result, err := i.db.ExecContext(ctx, query, id)
	if err != nil {
		log.Println("Error executing DELETE query:", err)
		return fmt.Errorf("failed to delete Topic: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		log.Println("Error getting rows affected:", err)
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		log.Println("No Topic found with ID:", id)
		return ErrTopicNotFound
	}

	return nil
}
