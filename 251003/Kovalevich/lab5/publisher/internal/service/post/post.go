package Post

import (
	"context"
	"lab3/publisher/internal/storage"
	"log"
	"time"

	"lab3/publisher/internal/mapper"
	PostModel "lab3/publisher/internal/model"
)

type httpClient interface {
	CreatePost(ctx context.Context, topicID int64, content string) (*PostModel.Post, error)
	GetPosts(ctx context.Context) ([]PostModel.Post, error)
	GetPost(ctx context.Context, id int64) (*PostModel.Post, error)
	UpdatePost(ctx context.Context, id, topicID int64, content string) (*PostModel.Post, error)
	DeletePost(ctx context.Context, id int64) error
}

type service struct {
	client httpClient
	cache  storage.Cache
}

type PostService interface {
	CreatePost(ctx context.Context, Post PostModel.Post) (PostModel.Post, error)
	GetPosts(ctx context.Context) ([]PostModel.Post, error)
	GetPostByID(ctx context.Context, id int64) (PostModel.Post, error)
	UpdatePostByID(ctx context.Context, Post PostModel.Post) (PostModel.Post, error)
	DeletePostByID(ctx context.Context, id int64) error
}

func New(client httpClient, cache storage.Cache) PostService {
	return &service{
		client: client,
		cache:  cache,
	}
}

func (s *service) CreatePost(ctx context.Context, Post PostModel.Post) (PostModel.Post, error) {
	createdMsg, err := s.client.CreatePost(ctx, int64(Post.TopicID), Post.Content)
	if err != nil {
		return PostModel.Post{}, err
	}

	log.Println(createdMsg)

	return mapper.MapHTTPPostToModel(*createdMsg), nil
}

func (s *service) GetPosts(ctx context.Context) ([]PostModel.Post, error) {
	cachedMsgs, err := s.cache.DB.GetAllMessages(ctx)
	if err == nil && len(cachedMsgs) > 0 {
		return cachedMsgs, nil
	}

	msgs, err := s.client.GetPosts(ctx)
	if err != nil {
		return nil, err
	}

	var mappedMessages []PostModel.Post
	for _, msg := range msgs {
		mappedMsg := mapper.MapHTTPPostToModel(msg)
		mappedMessages = append(mappedMessages, mappedMsg)

		if err := s.cache.DB.SaveMessage(ctx, &mappedMsg, 24*time.Hour); err != nil {
			log.Printf("failed to cache message %d: %v", mappedMsg.ID, err)
		}
	}

	if len(mappedMessages) == 0 {
		return []PostModel.Post{}, nil
	}

	return mappedMessages, nil
}

func (s *service) GetPostByID(ctx context.Context, id int64) (PostModel.Post, error) {
	cachedMsg, err := s.cache.DB.GetMessage(ctx, int(id))
	if err == nil && cachedMsg != nil {
		return *cachedMsg, nil
	}

	msg, err := s.client.GetPost(ctx, id)
	if err != nil {
		return PostModel.Post{}, err
	}

	mappedMsg := mapper.MapHTTPPostToModel(*msg)

	if err := s.cache.DB.SaveMessage(ctx, &mappedMsg, 24*time.Hour); err != nil {
		log.Printf("failed to cache message %d: %v", id, err)
	}

	return mappedMsg, nil
}

func (s *service) UpdatePostByID(ctx context.Context, Post PostModel.Post) (PostModel.Post, error) {
	updatedMsg, err := s.client.UpdatePost(ctx, int64(Post.ID), int64(Post.TopicID), Post.Content)
	if err != nil {
		return PostModel.Post{}, err
	}

	mappedMsg := mapper.MapHTTPPostToModel(*updatedMsg)

	if err := s.cache.DB.SaveMessage(ctx, &mappedMsg, 24*time.Hour); err != nil {
		log.Printf("failed to update cached message %d: %v", mappedMsg.ID, err)
	}

	return mappedMsg, nil
}

func (s *service) DeletePostByID(ctx context.Context, id int64) error {
	if err := s.client.DeletePost(ctx, id); err != nil {
		return err
	}

	if err := s.cache.DB.DeleteMessage(ctx, int(id)); err != nil {
		log.Printf("failed to delete cached message %d: %v", id, err)
	}

	return nil
}
