package storage

import (
	"github.com/jmoiron/sqlx"
	"distributedcomputing/model"
)
type MarkStorage struct {
	db *sqlx.DB
}

func NewMarkStorage(db *sqlx.DB) *MarkStorage {
	return &MarkStorage{db: db}
}

func (s *MarkStorage) Create(mark model.Mark) (int64, error) {
	query := `INSERT INTO tbl_mark (name) VALUES ($1) RETURNING id`
	var id int64
	err := s.db.QueryRow(query, mark.Name).Scan(&id)
	return id, err
}

func (s *MarkStorage) Get(id int64) (model.Mark, error) {
	var mark model.Mark
	query := `SELECT id, name FROM tbl_mark WHERE id = $1`
	err := s.db.Get(&mark, query, id)
	return mark, err
}

func (s *MarkStorage) GetAll() ([]model.Mark, []int64, error) {
	var marks []model.Mark
	var ids []int64
	query := `SELECT id, name FROM tbl_mark`
	err := s.db.Select(&marks, query)
	for _, m := range marks {
		ids = append(ids, m.Id)
	}
	return marks, ids, err
}

func (s *MarkStorage) Update(id int64, mark model.Mark) error {
	query := `UPDATE tbl_mark SET name=$1 WHERE id=$2`
	_, err := s.db.Exec(query, mark.Name, id)
	return err
}

func (s *MarkStorage) Delete(id int64) error {
	query := `DELETE FROM tbl_mark WHERE id=$1`
	res, err := s.db.Exec(query, id)
	if err == nil {
		n, _ := res.RowsAffected()
		if n == 0 {
			return &DeleteError{}
		}
	}

	return err
}
