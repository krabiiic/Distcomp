package model

import (
	"errors"
	"strings"
)

var (
	ErrInvalidNoticeContent = errors.New("content must be between 2 and 2048 characters")
)

type Notice struct {
	ID      int    `json:"id"`
	TopicID int    `json:"topicId"`
	Content string `json:"content"`
}

func (m *Notice) Validate() error {
	if len(strings.TrimSpace(m.Content)) < 2 || len(m.Content) > 2048 {
		return ErrInvalidNoticeContent
	}
	return nil
}
