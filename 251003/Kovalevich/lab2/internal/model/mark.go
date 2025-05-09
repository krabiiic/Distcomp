package model

import (
	"errors"
	"strings"
)

var (
	ErrInvalidMarkContent = errors.New("name must be between 2 and 32 characters")
)

type Mark struct {
	ID   int64  `json:"id" db:"id"`
	Name string `json:"name" db:"name"`
}

func (m *Mark) Validate() error {
	if len(strings.TrimSpace(m.Name)) < 2 || len(m.Name) > 2048 {
		return ErrInvalidMarkContent
	}

	return nil
}
