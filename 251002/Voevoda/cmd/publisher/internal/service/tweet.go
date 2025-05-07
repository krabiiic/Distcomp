package service

import (
	"errors"

	"github.com/strcarne/distributed-calculations/internal/entity"
)

var (
	ErrInvalidTitle   = errors.New("invalid title")
	ErrInvalidContent = errors.New("invalid content")
)

type Tweet struct {
	repo TweetRepository
}

type TweetRepository interface {
	CreateTweet(tweet entity.Tweet) (int64, error)
	DeleteTweet(id int64) (entity.Tweet, error)
	GetAllTweets() ([]entity.Tweet, error)
	GetTweetByID(id int64) (entity.Tweet, error)
	UpdateTweet(tweet entity.Tweet) error
}

func NewTweet(repo TweetRepository) Tweet {
	return Tweet{
		repo: repo,
	}
}

func (u Tweet) CreateTweet(tweet entity.Tweet) (entity.Tweet, error) {
	id, err := u.repo.CreateTweet(tweet)
	if err != nil {
		return entity.Tweet{}, err
	}

	tweet.ID = id

	return tweet, nil
}

func (u Tweet) DeleteTweet(id int64) (entity.Tweet, error) {
	deletedTweet, err := u.repo.DeleteTweet(id)
	if err != nil {
		return entity.Tweet{}, err
	}

	return deletedTweet, nil
}

func (u Tweet) GetAllTweets() ([]entity.Tweet, error) {
	tweets, err := u.repo.GetAllTweets()
	if err != nil {
		return nil, err
	}

	return tweets, nil
}

func (u Tweet) GetTweetByID(id int64) (entity.Tweet, error) {
	tweet, err := u.repo.GetTweetByID(id)
	if err != nil {
		return entity.Tweet{}, err
	}

	return tweet, nil
}

func (u Tweet) UpdateTweet(tweet entity.Tweet) error {
	if len(tweet.Title) < 2 {
		return ErrInvalidTitle
	}

	if len(tweet.Content) < 4 {
		return ErrInvalidContent
	}

	err := u.repo.UpdateTweet(tweet)
	if err != nil {
		return err
	}

	return nil
}
