package msg

import (
	"github.com/google/uuid"
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type Method string

const (
	MethodCreate Method = "create"
	MethodUpdate Method = "update"
	MethodDelete Method = "delete"
	MethodGet    Method = "get"
	MethodGetAll Method = "get_all"
)

type NoteRequest struct {
	CorrelationID uuid.UUID   `json:"correlation_id"`
	Method        Method      `json:"method"`
	Note          entity.Note `json:"note"`
}

func NewNoteRequest(method Method, note entity.Note) NoteRequest {
	return NoteRequest{
		CorrelationID: uuid.New(),
		Method:        method,
		Note:          note,
	}
}

type NoteResponse struct {
	CorrelationID uuid.UUID      `json:"correlation_id"`
	Note          *entity.Note   `json:"note,omitempty"`
	Notes         []entity.Note `json:"notes,omitempty"`
	Error         *string        `json:"error,omitempty"`
}
