package server

type Error struct {
	Error string `json:"error"`
	Code  int    `json:"code"`
}

func NewError(err error, code int) *Error {
	return &Error{
		Error: err.Error(),
		Code:  code,
	}
}
