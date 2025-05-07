package entity

import "net/mail"

const MinPasswordLength = 8

type User struct {
	ID        int64  `json:"id,omitempty"`
	Login     string `json:"login"`
	Password  string `json:"password"`
	FirstName string `json:"firstname"`
	LastName  string `json:"lastname"`
}

func (u User) IsValid() bool {
	if u.ID < 0 {
		return false
	}

	_, err := mail.ParseAddress(u.Login)
	if err != nil {
		return false
	}

	if len(u.Password) < MinPasswordLength {
		return false
	}

	if len(u.FirstName) == 0 {
		return false
	}

	if len(u.LastName) == 0 {
		return false
	}

	return true
}
