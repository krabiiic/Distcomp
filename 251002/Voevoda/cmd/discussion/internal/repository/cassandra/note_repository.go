package cassandra

import (
	"fmt"
	"log/slog"
	"time"

	"github.com/gocql/gocql"
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type NoteRepository struct {
	session *gocql.Session
	logger  *slog.Logger
}

func NewNoteRepository(session *gocql.Session, logger *slog.Logger) NoteRepository {
	logger.Info("Creating note repository table if not exists")

	err := session.Query(`CREATE TABLE IF NOT EXISTS tbl_note (
	    country text,
	    tweetId bigint,
	    id bigint,
	    content text,
	    PRIMARY KEY ((country), tweetId, id)
	) WITH CLUSTERING ORDER BY (tweetId ASC, id ASC)
	AND comment = 'Size limit of content: 2-2048 characters';`).Exec()
	if err != nil {
		logger.Error("Failed to create note table", slog.String("error", err.Error()))
	} else {
		logger.Info("Note table created or already exists")
	}

	return NoteRepository{session: session, logger: logger}
}

// CreateNote inserts a new note into the database
func (r NoteRepository) CreateNote(note entity.Note) (int64, error) {
	if note.Country == "" {
		note.Country = "by"
	}

	r.logger.Info("Creating new note",
		slog.String("country", note.Country),
		slog.Int("tweetID", note.TweetID),
		slog.String("content", note.Content))

	// Generate a unique ID for the note
	id := time.Now().UnixNano()
	r.logger.Debug("Generated note ID", slog.Int64("id", id))

	// Insert the note into the database
	err := r.session.Query(`
		INSERT INTO distcomp.tbl_note (country, tweetId, id, content)
		VALUES (?, ?, ?, ?)`,
		note.Country, note.TweetID, id, note.Content).Exec()
	if err != nil {
		r.logger.Error("Failed to insert note",
			slog.String("error", err.Error()),
			slog.Int64("id", id),
			slog.String("country", note.Country),
			slog.Int("tweetID", note.TweetID))
		return 0, err
	}

	r.logger.Info("Note created successfully", slog.Int64("id", id))
	return id, nil
}

// DeleteNote removes a note from the database by ID
func (r NoteRepository) DeleteNote(id int64) (entity.Note, error) {
	r.logger.Info("Deleting note", slog.Int64("id", id))

	// First get the note to return it
	note, err := r.GetNoteByID(id)
	if err != nil {
		r.logger.Error(
			"Failed to get note for deletion",
			slog.Int64("id", id),
			slog.String("error", err.Error()),
		)
		return entity.Note{}, err
	}

	r.logger.Debug("Found note to delete",
		slog.Int64("id", id),
		slog.String("country", note.Country),
		slog.Int("tweetID", note.TweetID))

	// Delete the note
	err = r.session.Query(`
		DELETE FROM distcomp.tbl_note 
		WHERE country = ? AND tweetId = ? AND id = ?`,
		note.Country, note.TweetID, id).Exec()

	if err != nil {
		r.logger.Error("Failed to delete note", slog.Int64("id", id), slog.String("error", err.Error()))
	} else {
		r.logger.Info("Note deleted successfully", slog.Int64("id", id))
	}

	return note, err
}

// GetAllNotes retrieves all notes from the database
func (r NoteRepository) GetAllNotes() ([]entity.Note, error) {
	r.logger.Info("Retrieving all notes")
	notes := make([]entity.Note, 0, 4)

	// Query all notes
	iter := r.session.Query(`SELECT country, tweetId, id, content FROM distcomp.tbl_note`).Iter()

	var note entity.Note
	count := 0
	for iter.Scan(&note.Country, &note.TweetID, &note.ID, &note.Content) {
		notes = append(notes, note)
		count++
		r.logger.Debug("Retrieved note",
			slog.Int64("id", note.ID),
			slog.String("country", note.Country),
			slog.Int("tweetID", note.TweetID))
		// Reset note for next iteration
		note = entity.Note{}
	}

	if err := iter.Close(); err != nil {
		r.logger.Error(
			"Failed to close iterator when retrieving all notes",
			slog.String("error", err.Error()),
		)
		return nil, err
	}

	r.logger.Info("Retrieved all notes", slog.Int("count", count))
	return notes, nil
}

// GetNoteByID retrieves a specific note by its ID
func (r NoteRepository) GetNoteByID(id int64) (entity.Note, error) {
	r.logger.Info("Retrieving note by ID", slog.Int64("id", id))
	var note entity.Note

	// We need to query with a specific country and tweetId, but we only have the ID
	// This is a limitation of the current schema design
	// A more efficient approach would be to add a secondary index on id
	iter := r.session.Query(`
		SELECT country, tweetId, id, content 
		FROM distcomp.tbl_note 
		WHERE id = ? ALLOW FILTERING`,
		id).Iter()

	if !iter.Scan(&note.Country, &note.TweetID, &note.ID, &note.Content) {
		r.logger.Error("Note not found", slog.Int64("id", id))
		return entity.Note{}, fmt.Errorf("note with id %d not found", id)
	}

	if err := iter.Close(); err != nil {
		r.logger.Error("Failed to close iterator when retrieving note by ID",
			slog.Int64("id", id),
			slog.String("error", err.Error()))
		return entity.Note{}, err
	}

	r.logger.Info("Retrieved note successfully",
		slog.Int64("id", id),
		slog.String("country", note.Country),
		slog.Int("tweetID", note.TweetID))
	return note, nil
}

// UpdateNote updates an existing note in the database
func (r NoteRepository) UpdateNote(note entity.Note) error {
	if note.Country == "" {
		note.Country = "by"
	}

	r.logger.Info("Updating note",
		slog.Int64("id", note.ID),
		slog.String("country", note.Country),
		slog.Int("tweetID", note.TweetID))

	// First check if the note exists
	_, err := r.GetNoteByID(note.ID)
	if err != nil {
		r.logger.Error("Failed to find note for update",
			slog.Int64("id", note.ID),
			slog.String("error", err.Error()))
		return err
	}

	// Update the note
	err = r.session.Query(`
		UPDATE distcomp.tbl_note 
		SET content = ? 
		WHERE country = ? AND tweetId = ? AND id = ?`,
		note.Content, note.Country, note.TweetID, note.ID).Exec()

	if err != nil {
		r.logger.Error("Failed to update note",
			slog.Int64("id", note.ID),
			slog.String("error", err.Error()))
	} else {
		r.logger.Info("Note updated successfully", slog.Int64("id", note.ID))
	}

	return err
}
