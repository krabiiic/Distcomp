package entity

import "time"

type Tweet struct {
	ID       int64     `json:"id,omitempty"`
	UserID   int       `json:"userId"`
	Title    string    `json:"title"`
	Content  string    `json:"content"`
	Created  time.Time `json:"created"`
	Modified time.Time `json:"modified"`
}
