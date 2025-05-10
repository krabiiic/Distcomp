package model

import "time"

type Writer struct {
	ID        int64  `db:"id"`
	Login     string `db:"login"`
	Password  string `db:"password"`
	FirstName string `db:"firstname"`
	LastName  string `db:"lastname"`
}

type Topic struct {
	ID       int64      `db:"id"`
	WriterID int64      `db:"writer_id"`
	Title    string     `db:"title"`
	Content  string     `db:"content"`
	Created  time.Time  `db:"created"`
	Modified *time.Time `db:"modified"`
	Marks    []string
}

type Post struct {
	ID      int64  `db:"id"`
	TopicID int64  `db:"topicid"`
	Content string `db:"content"`
}

type Mark struct {
	ID   int64  `db:"id"`
	Name string `db:"name"`
}
