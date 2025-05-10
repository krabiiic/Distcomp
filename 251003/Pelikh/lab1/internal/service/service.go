package service

import (
	"lab1/internal/service/creator"
	"lab1/internal/service/marker"
	"lab1/internal/service/notice"
	"lab1/internal/service/topic"
	"lab1/internal/storage"
)

type Service struct {
	Creator creator.Service
	Topic   topic.Service
	Notice  notice.Service
	Marker  marker.Service
}

func New(db storage.Storage) Service {

	return Service{
		Creator: creator.New(db.Creator),
		Topic:   topic.New(db.Topic),
		Notice:  notice.New(db.Notice),
		Marker:  marker.New(db.Marker),
	}
}
