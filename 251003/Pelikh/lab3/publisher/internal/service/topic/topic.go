package topic

import (
	"context"
	"log"

	"lab3/publisher/internal/mapper"
	topicModel "lab3/publisher/internal/model"
	dbTopicModel "lab3/publisher/internal/storage/model"
)

type service struct {
	db TopicDB
}

type TopicDB interface {
	CreateTopic(ctx context.Context, Topic dbTopicModel.Topic) (dbTopicModel.Topic, error)
	GetTopics(ctx context.Context) ([]dbTopicModel.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (dbTopicModel.Topic, error)
	UpdateTopicByID(ctx context.Context, Topic dbTopicModel.Topic) (dbTopicModel.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

type TopicService interface {
	CreateTopic(ctx context.Context, Topic topicModel.Topic) (topicModel.Topic, error)
	GetTopics(ctx context.Context) ([]topicModel.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (topicModel.Topic, error)
	UpdateTopicByID(ctx context.Context, Topic topicModel.Topic) (topicModel.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

func New(db TopicDB) TopicService {
	return &service{
		db: db,
	}
}

func (s *service) CreateTopic(ctx context.Context, Topic topicModel.Topic) (topicModel.Topic, error) {
	mappedTopic, err := mapper.MapTopicToModel(Topic)
	if err != nil {
		log.Println(err)
		return topicModel.Topic{}, err
	}

	mappedData, err := s.db.CreateTopic(ctx, mappedTopic)
	if err != nil {
		return topicModel.Topic{}, err
	}

	return mapper.MapModelToTopic(mappedData), nil
}

func (s *service) GetTopics(ctx context.Context) ([]topicModel.Topic, error) {
	topics, err := s.db.GetTopics(ctx)
	if err != nil {
		return nil, err
	}

	var mappedTopics []topicModel.Topic

	for _, topic := range topics {
		mappedTopics = append(mappedTopics, mapper.MapModelToTopic(topic))
	}

	if len(mappedTopics) == 0 {
		mappedTopics = []topicModel.Topic{}
	}

	return mappedTopics, nil
}

func (s *service) GetTopicByID(ctx context.Context, id int64) (topicModel.Topic, error) {
	topic, err := s.db.GetTopicByID(ctx, id)
	if err != nil {
		return topicModel.Topic{}, err
	}

	return mapper.MapModelToTopic(topic), nil
}

func (s *service) UpdateTopicByID(ctx context.Context, Topic topicModel.Topic) (topicModel.Topic, error) {
	mappedTopic, err := mapper.MapTopicToModel(Topic)
	if err != nil {
		return topicModel.Topic{}, err
	}

	updatedTopic, err := s.db.UpdateTopicByID(ctx, mappedTopic)
	if err != nil {
		return topicModel.Topic{}, err
	}

	return mapper.MapModelToTopic(updatedTopic), nil
}

func (s *service) DeleteTopicByID(ctx context.Context, id int64) error {
	return s.db.DeleteTopicByID(ctx, id)
}
