package model

import "time"

type Creator struct {
	Id        int64  `db:"id"`
	Login     string `db:"login"`
	Password  string `db:"password"`
	FirstName string `db:"firstname"`
	LastName  string `db:"lastname"`
}

type Story struct {
	Id        int64     `db:"id"`
	CreatorID int64     `db:"creator_id"`
	Title     string    `db:"title"`
	Content   string    `db:"content"`
	Created   time.Time `db:"created"`
	Modified  time.Time `db:"modified"`
}

type Note struct {
	Id      int64  `db:"id"`
	StoryID int64  `db:"story_id"`
	Content string `db:"content"`
}

type Mark struct {
	Id   int64  `db:"id"`
	Name string `db:"name"`
}
