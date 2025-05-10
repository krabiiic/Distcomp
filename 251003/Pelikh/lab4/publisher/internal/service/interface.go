package service

import (
	"context"

	m "lab3/publisher/internal/model"
)

type CreatorService interface {
	CreateCreator(ctx context.Context, cr m.Creator) (m.Creator, error)
	GetCreators(ctx context.Context) ([]m.Creator, error)
	GetCreatorByID(ctx context.Context, id int) (m.Creator, error)
	UpdateCreatorByID(ctx context.Context, cr m.Creator) (m.Creator, error)
	DeleteCreatorByID(ctx context.Context, id int) error
}

type TopicService interface {
	CreateTopic(ctx context.Context, topic m.Topic) (m.Topic, error)
	GetTopics(ctx context.Context) ([]m.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (m.Topic, error)
	UpdateTopicByID(ctx context.Context, topic m.Topic) (m.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

type NoticeService interface {
	CreateNotice(ctx context.Context, notice m.Notice) (m.Notice, error)
	GetNotices(ctx context.Context) ([]m.Notice, error)
	GetNoticeByID(ctx context.Context, id int64) (m.Notice, error)
	UpdateNoticeByID(ctx context.Context, notice m.Notice) (m.Notice, error)
	DeleteNoticeByID(ctx context.Context, id int64) error
}

type MarkerService interface {
	CreateMarker(ctx context.Context, marker m.Marker) (m.Marker, error)
	GetMarkers(ctx context.Context) ([]m.Marker, error)
	GetMarkerByID(ctx context.Context, id int64) (m.Marker, error)
	UpdateMarkerByID(ctx context.Context, marker m.Marker) (m.Marker, error)
	DeleteMarkerByID(ctx context.Context, id int64) error
}
