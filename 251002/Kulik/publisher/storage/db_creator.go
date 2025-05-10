package storage

import (
	"distributedcomputing/model"
	"fmt"

	"github.com/jmoiron/sqlx"
)

type CreatorStorage struct {
	db *sqlx.DB
}

func NewCreatorStorage(db *sqlx.DB) *CreatorStorage {
	return &CreatorStorage{db: db}
}

func (s *CreatorStorage) Create(c model.Creator) (int64, error) {
	query := `INSERT INTO tbl_creator (login, password, firstname, lastname) VALUES ($1, $2, $3, $4) RETURNING id`
	var id int64
	err := s.db.QueryRow(query, c.Login, c.Password, c.FirstName, c.LastName).Scan(&id)
	fmt.Println(err)
	return id, err
}

func (s *CreatorStorage) Get(id int64) (model.Creator, error) {
	var c model.Creator
	query := `SELECT id, login, password, firstname, lastname FROM tbl_creator WHERE id = $1`
	err := s.db.Get(&c, query, id)
	fmt.Println("get creat ", err)
	return c, err
}

func (s *CreatorStorage) GetAll() ([]model.Creator, []int64, error) {
	var creators []model.Creator
	var ids []int64
	query := `SELECT id, login, password, firstname, lastname FROM tbl_creator`
	err := s.db.Select(&creators, query)
	for _, c := range creators {
		ids = append(ids, c.Id)
	}
	return creators, ids, err
}

func (s *CreatorStorage) Update(id int64, c model.Creator) error {
	query := `UPDATE tbl_creator SET login=$1, password=$2, firstname=$3, lastname=$4 WHERE id=$5`
	_, err := s.db.Exec(query, c.Login, c.Password, c.FirstName, c.LastName, id)
	return err
}

func (s *CreatorStorage) Delete(id int64) error {
	query := `DELETE FROM tbl_creator WHERE id=$1`
	res, err := s.db.Exec(query, id)
	if err == nil {
		n, _ := res.RowsAffected()
		if n == 0 {
			return &DeleteError{}
		}
	}

	return err
}
