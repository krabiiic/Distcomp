package model

import (
	"errors"
	"strings"
)

var (
	ErrInvalidMarkerContent = errors.New("name must be between 2 and 32 characters")
)

type Marker struct {
	ID   int64  `json:"id" db:"id"`
	Name string `json:"name" db:"name"`
}

func (m *Marker) Validate() error {
	if len(strings.TrimSpace(m.Name)) < 2 || len(m.Name) > 2048 {
		return ErrInvalidMarkerContent
	}

	return nil
}
