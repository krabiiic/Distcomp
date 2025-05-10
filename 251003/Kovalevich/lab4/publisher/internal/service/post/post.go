package Post

import (
	"context"
	"log"

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
}

type PostService interface {
	CreatePost(ctx context.Context, Post PostModel.Post) (PostModel.Post, error)
	GetPosts(ctx context.Context) ([]PostModel.Post, error)
	GetPostByID(ctx context.Context, id int64) (PostModel.Post, error)
	UpdatePostByID(ctx context.Context, Post PostModel.Post) (PostModel.Post, error)
	DeletePostByID(ctx context.Context, id int64) error
}

func New(client httpClient) PostService {
	return &service{
		client: client,
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
	var mappedPosts []PostModel.Post

	msgs, err := s.client.GetPosts(ctx)
	if err != nil {
		return mappedPosts, err
	}

	for _, msg := range msgs {
		mappedPosts = append(mappedPosts, mapper.MapHTTPPostToModel(msg))
	}

	if len(mappedPosts) == 0 {
		return []PostModel.Post{}, nil
	}

	return mappedPosts, nil
}

func (s *service) GetPostByID(ctx context.Context, id int64) (PostModel.Post, error) {
	msg, err := s.client.GetPost(ctx, id)
	if err != nil {
		return PostModel.Post{}, err
	}

	return mapper.MapHTTPPostToModel(*msg), nil
}

func (s *service) UpdatePostByID(ctx context.Context, Post PostModel.Post) (PostModel.Post, error) {
	updatedMsg, err := s.client.UpdatePost(ctx, int64(Post.ID), int64(Post.TopicID), Post.Content)
	if err != nil {
		return PostModel.Post{}, err
	}

	return mapper.MapHTTPPostToModel(*updatedMsg), nil
}

func (s *service) DeletePostByID(ctx context.Context, id int64) error {
	return s.client.DeletePost(ctx, id)
}
