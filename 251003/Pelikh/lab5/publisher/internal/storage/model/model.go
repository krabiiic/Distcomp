package model

import "time"

type Creator struct {
	ID        int64  `db:"id"`
	Login     string `db:"login"`
	Password  string `db:"password"`
	FirstName string `db:"firstname"`
	LastName  string `db:"lastname"`
}

type Topic struct {
	ID        int64      `db:"id"`
	CreatorID int64      `db:"creator_id"`
	Title     string     `db:"title"`
	Content   string     `db:"content"`
	Created   time.Time  `db:"created"`
	Modified  *time.Time `db:"modified"`
	Markers   []string
}

type Notice struct {
	ID      int64  `db:"id"`
	TopicID int64  `db:"topic_id"` /////////////////////////////
	Content string `db:"content"`
}

type Marker struct {
	ID   int64  `db:"id"`
	Name string `db:"name"`
}
