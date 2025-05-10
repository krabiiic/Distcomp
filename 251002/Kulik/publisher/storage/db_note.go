package storage

import (
	"github.com/jmoiron/sqlx"
	"fmt"
	"distributedcomputing/model"
)


type NoteStorage struct {
	db *sqlx.DB
}

func NewNoteStorage(db *sqlx.DB) *NoteStorage {
	return &NoteStorage{db: db}
}

func (s *NoteStorage) Create(note model.Note) (int64, error) {
	query := `INSERT INTO tbl_note (story_id, content) VALUES ($1, $2) RETURNING id`
	var id int64
	err := s.db.QueryRow(query, note.StoryID, note.Content).Scan(&id)
	return id, err
}

func (s *NoteStorage) Get(id int64) (model.Note, error) {
	var note model.Note
	query := `SELECT id, story_id, content FROM tbl_note WHERE id = $1`
	err := s.db.Get(&note, query, id)
	return note, err
}

func (s *NoteStorage) GetAll() ([]model.Note, []int64, error) {
	var notes []model.Note
	var ids []int64
	query := `SELECT id, story_id, content FROM tbl_note`
	err := s.db.Select(&notes, query)
	for _, n := range notes {
		ids = append(ids, n.Id)
	}
	return notes, ids, err
}

func (s *NoteStorage) Update(id int64, note model.Note) error {
	query := `UPDATE tbl_note SET content=$1, story_id=$2 WHERE id=$3`
	_, err := s.db.Exec(query, note.Content, note.StoryID, id)
	fmt.Println("Error: ", err)
	return err
}


func (s *NoteStorage) Delete(id int64) error {
	query := `DELETE FROM tbl_note WHERE id=$1`
	res, err := s.db.Exec(query, id)
	if err == nil {
		n, _ := res.RowsAffected()
		if n == 0 {
			return &DeleteError{}
		}
	}

	return err
}