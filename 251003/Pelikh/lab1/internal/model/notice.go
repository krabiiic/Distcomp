package model

import (
	"errors"
	"strings"
)

var (
	ErrInvalidCommentContent = errors.New("content must be between 2 and 2048 characters")
)

type Notice struct {
	ID      int64  `json:"id" db:"id"`
	TopicID int64  `json:"topicId" db:"topic_id"`
	Content string `json:"content" db:"content"`
}

func (m *Notice) Validate() error {
	if len(strings.TrimSpace(m.Content)) < 2 || len(m.Content) > 2048 {
		return ErrInvalidCommentContent
	}

	return nil
}
