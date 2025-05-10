package storage

import (
	"distributedcomputing/model"
	"fmt"

	"github.com/jmoiron/sqlx"
)

type StoryStorage struct {
	db *sqlx.DB
}

func NewStoryStorage(db *sqlx.DB) *StoryStorage {
	return &StoryStorage{db: db}
}

func (s *StoryStorage) Create(story model.Story) (int64, error) {
	query := `INSERT INTO tbl_story (creator_id, title, content, created, modified) VALUES ($1, $2, $3, $4, $5) RETURNING id`
	var id int64
	err := s.db.QueryRow(query, story.CreatorID, story.Title, story.Content, story.Created, story.Modified).Scan(&id)
	return id, err
}

func (s *StoryStorage) Get(id int64) (model.Story, error) {
	var story model.Story
	query := `SELECT id, creator_id, title, content, created, modified FROM tbl_story WHERE id = $1`
	err := s.db.Get(&story, query, id)
	return story, err
}


func (s *StoryStorage) GetAll() ([]model.Story, []int64, error) {
	var stories []model.Story
	var ids []int64
	query := `SELECT id, creator_id, title, content, created, modified FROM tbl_story`
	err := s.db.Select(&stories, query)
	for _, s := range stories {
		ids = append(ids, s.Id)
	}
	fmt.Println("get all stories ", err)
	return stories, ids, err
}

func (s *StoryStorage) Update(id int64, story model.Story) error {
	query := `UPDATE tbl_story SET title=$1, content=$2, modified=$3 WHERE id=$4`
	_, err := s.db.Exec(query, story.Title, story.Content, story.Modified, id)
	return err
}

func (s *StoryStorage) Delete(id int64) error {
	query := `DELETE FROM tbl_story WHERE id=$1`
	res, err := s.db.Exec(query, id)
	if err == nil {
		n, _ := res.RowsAffected()
		if n == 0 {
			return &DeleteError{}
		}
	}

	return err
}


