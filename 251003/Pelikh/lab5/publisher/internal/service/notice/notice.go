package notice

import (
	"context"
	"lab3/publisher/internal/storage"
	"log"
	"time"

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
	cache  storage.Cache
}

type NoticeService interface {
	CreateNotice(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error)
	GetNotices(ctx context.Context) ([]noticeModel.Notice, error)
	GetNoticeByID(ctx context.Context, id int64) (noticeModel.Notice, error)
	UpdateNoticeByID(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error)
	DeleteNoticeByID(ctx context.Context, id int64) error
}

func New(client httpClient, cache storage.Cache) NoticeService {
	return &service{
		client: client,
		cache:  cache,
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
	cachedMsgs, err := s.cache.DB.GetAllMessages(ctx)
	if err == nil && len(cachedMsgs) > 0 {
		return cachedMsgs, nil
	}

	notices, err := s.client.GetNotices(ctx)
	if err != nil {
		return nil, err
	}

	var mappedNotices []noticeModel.Notice

	//notices, err := s.client.GetNotices(ctx)
	/*if err != nil {
		return mappedNotices, err
	}*/

	for _, notice := range notices {
		mappedNts := mapper.MapHTTPNoticeToModel(notice)
		mappedNotices = append(mappedNotices, mappedNts)

		if err := s.cache.DB.SaveMessage(ctx, &mappedNts, 24*time.Hour); err != nil {
			log.Printf("failed to cache message %d: %v", mappedNts.ID, err)
		}
	}

	if len(mappedNotices) == 0 {
		return []noticeModel.Notice{}, nil
	}

	return mappedNotices, nil
}

func (s *service) GetNoticeByID(ctx context.Context, id int64) (noticeModel.Notice, error) {
	cachedMsg, err := s.cache.DB.GetMessage(ctx, int(id))
	if err == nil && cachedMsg != nil {
		return *cachedMsg, nil
	}

	notice, err := s.client.GetNotice(ctx, id)
	if err != nil {
		return noticeModel.Notice{}, err
	}

	mappedNts := mapper.MapHTTPNoticeToModel(*notice)

	if err := s.cache.DB.SaveMessage(ctx, &mappedNts, 24*time.Hour); err != nil {
		log.Printf("failed to cache message %d: %v", id, err)
	}

	return mappedNts, nil
}

func (s *service) UpdateNoticeByID(ctx context.Context, notice noticeModel.Notice) (noticeModel.Notice, error) {
	updatedNotice, err := s.client.UpdateNotice(ctx, int64(notice.ID), int64(notice.TopicID), notice.Content)
	if err != nil {
		return noticeModel.Notice{}, err
	}

	mappedNts := mapper.MapHTTPNoticeToModel(*updatedNotice)

	if err := s.cache.DB.SaveMessage(ctx, &mappedNts, 24*time.Hour); err != nil {
		log.Printf("failed to update cached message %d: %v", mappedNts.ID, err)
	}

	return mappedNts, nil
}

func (s *service) DeleteNoticeByID(ctx context.Context, id int64) error {
	if err := s.client.DeleteNotice(ctx, id); err != nil {
		return err
	}

	if err := s.cache.DB.DeleteMessage(ctx, int(id)); err != nil {
		log.Printf("failed to delete cached message %d: %v", id, err)
	}

	return nil
}
