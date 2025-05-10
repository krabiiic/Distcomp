package service

import (
	"lab3/publisher/internal/api/discussion"
	"lab3/publisher/internal/service/creator"
	"lab3/publisher/internal/service/marker"
	"lab3/publisher/internal/service/notice"
	//"lab3/publisher/internal/service/notice"
	"lab3/publisher/internal/service/topic"
	"lab3/publisher/internal/storage"
)

type Service struct {
	db storage.Storage

	Creator CreatorService
	Topic   TopicService
	Marker  MarkerService
	Notice  NoticeService
}

func New(db storage.Storage) Service {
	return Service{
		db: db,

		Creator: creator.New(db.DB.CreatorInst),
		Topic:   topic.New(db.DB.TopicInst),
		Marker:  marker.New(db.DB.MarkerInst),
		Notice:  notice.New(discussion.NewClient(), *storage.NewCache()),
	}
}
