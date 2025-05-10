package notice

import (
	"context"
	"log"

	"lab3/publisher/internal/mapper"
	noticeModel "lab3/publisher/internal/model"
)

type httpClient interface {
	CreateNotice(ctx context.Context, topicID int64, content string) (*noticeModel.Notice, error) //chacha
	GetNotices(ctx context.Context) ([]noticeModel.Notice, error)
	GetNotice(ctx context.Context, id int64) (*noticeModel.Notice, error)
	UpdateNotice(ctx context.Context, id, topicID int64, content string) (*noticeModel.Notice, error) //chacha
	DeleteNotice(ctx context.Context, id int64) error
}

type service struct {
	client httpClient
}

type NoticeService interface {
	CreateNotice(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error)
	GetNotices(ctx context.Context) ([]noticeModel.Notice, error)
	GetNoticeByID(ctx context.Context, id int64) (noticeModel.Notice, error)
	UpdateNoticeByID(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error)
	DeleteNoticeByID(ctx context.Context, id int64) error
}

func New(client httpClient) NoticeService {
	return &service{
		client: client,
	}
}

func (s *service) CreateNotice(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error) {
	createdNotice, err := s.client.CreateNotice(ctx, int64(notice.TopicID), notice.Content)
	if err != nil {
		return noticeModel.Notice{}, err
	}

	log.Println(createdNotice)

	return mapper.MapHTTPNoticeToModel(*createdNotice), nil
}

func (s *service) GetNotices(ctx context.Context) ([]noticeModel.Notice, error) {
	var mappedNotices []noticeModel.Notice

	notices, err := s.client.GetNotices(ctx)
	if err != nil {
		return mappedNotices, err
	}

	for _, notice := range notices {
		mappedNotices = append(mappedNotices, mapper.MapHTTPNoticeToModel(notice))
	}

	if len(mappedNotices) == 0 {
		return []noticeModel.Notice{}, nil
	}

	return mappedNotices, nil
}

func (s *service) GetNoticeByID(ctx context.Context, id int64) (noticeModel.Notice, error) {
	notice, err := s.client.GetNotice(ctx, id)
	if err != nil {
		return noticeModel.Notice{}, err
	}

	return mapper.MapHTTPNoticeToModel(*notice), nil
}

func (s *service) UpdateNoticeByID(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error) {
	updatedNotice, err := s.client.UpdateNotice(ctx, int64(notice.ID), int64(notice.TopicID), notice.Content)
	if err != nil {
		return noticeModel.Notice{}, err
	}

	return mapper.MapHTTPNoticeToModel(*updatedNotice), nil
}

func (s *service) DeleteNoticeByID(ctx context.Context, id int64) error {
	return s.client.DeleteNotice(ctx, id)
}
