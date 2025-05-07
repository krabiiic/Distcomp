package model

import "time"

type CreatorRequestTo struct {
	Id        int64  `json:"id"`
	Login     string `json:"login"`
	Password  string `json:"password"`
	FirstName string `json:"firstname"`
	LastName  string `json:"lastname"`
}

type StoryRequestTo struct {
	Id        int64  `json:"id"`
	CreatorID int64  `json:"creatorId"`
	Title     string `json:"title"`
	Content   string `json:"content"`
}

type NoteRequestTo struct {
	Id      int64  `json:"id"`
	StoryID int64  `json:"storyId"`
	Content string `json:"content"`
}

type MarkRequestTo struct {
	Id   int64  `json:"id"`
	Name string `json:"name"`
}

type CreatorResponseTo struct {
	Id        int64  `json:"id"`
	Login     string `json:"login"`
	FirstName string `json:"firstname"`
	LastName  string `json:"lastname"`
}

type StoryResponseTo struct {
	Id        int64     `json:"id"`
	CreatorID int64     `json:"creatorId"`
	Title     string    `json:"title"`
	Content   string    `json:"content"`
	Created   time.Time `json:"created"`
	Modified  time.Time `json:"modified"`
}

type NoteResponseTo struct {
	Id      int64  `json:"id"`
	StoryID int64  `json:"storyId"`
	Content string `json:"content"`
}

type MarkResponseTo struct {
	Id   int64  `json:"id"`
	Name string `json:"name"`
}
