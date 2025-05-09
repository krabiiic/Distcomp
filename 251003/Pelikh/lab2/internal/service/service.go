package service

import (
	"lab2/internal/service/creator"
	"lab2/internal/service/marker"
	"lab2/internal/service/notice"
	"lab2/internal/service/topic"
	"lab2/internal/storage"
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
