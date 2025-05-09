package models

type Story struct {
	Country string `json:"Country"`
	StoryID uint   `json:"storyId"`
	ID      uint   `json:"id"`
	Content string `json:"content"`
}
