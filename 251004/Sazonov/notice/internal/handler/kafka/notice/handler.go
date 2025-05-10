package notice

import (
	"context"
	"encoding/json"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/model"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/validator"
	"github.com/segmentio/kafka-go"
)

type noticeService interface {
	CreateNotice(ctx context.Context, notice model.Notice) (model.Notice, error)
}

type NoticeHandler struct {
	notice noticeService
}

func New(notice noticeService) *NoticeHandler {
	return &NoticeHandler{
		notice: notice,
	}
}

func (h *NoticeHandler) Handle(ctx context.Context, m kafka.Message) error {
	type request struct {
		NewsID  int64  `json:"news_id,omitempty" validate:"required,gt=0"`
		Content string `json:"content,omitempty" validate:"required,min=4"`
	}

	var req request
	if err := json.Unmarshal(m.Value, &req); err != nil {
		return err
	}

	if err := validator.Validtor().Struct(req); err != nil {
		return err
	}

	_, err := h.notice.CreateNotice(ctx, model.Notice{NewsID: req.NewsID, Content: req.Content})
	if err != nil {
		return err
	}

	return nil
}
