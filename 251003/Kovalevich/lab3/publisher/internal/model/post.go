package model

import (
	"errors"
	"strings"
)

var (
	ErrInvalidpostContent = errors.New("content must be between 2 and 2048 characters")
)

type Post struct {
	ID      int    `json:"id"`
	TopicID int    `json:"topicId"`
	Content string `json:"content"`
}

func (m *Post) Validate() error {
	if len(strings.TrimSpace(m.Content)) < 2 || len(m.Content) > 2048 {
		return ErrInvalidpostContent
	}

	return nil
}
