package inmemory

import (
	"errors"
	"sync/atomic"

	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/pkg/container"
)

var ErrTweetNotFound = errors.New("tweet not found")

type TweetRepository struct {
	storage *container.InMemoryStorage[int64, entity.Tweet]

	// counter is incremeted field that will be id of new tweettr.
	counter atomic.Int64
}

func NewTweetRepository() *TweetRepository {
	return &TweetRepository{
		storage: container.DefaultInMemoryStorage[int64, entity.Tweet](),
		counter: atomic.Int64{},
	}
}

func (tr *TweetRepository) createNewID() int64 {
	return tr.counter.Add(1)
}

func (tr *TweetRepository) CreateTweet(tweet entity.Tweet) (int64, error) {
	newID := tr.createNewID()
	tweet.ID = newID

	tr.storage.Set(newID, tweet)

	return newID, nil
}

func (tr *TweetRepository) DeleteTweet(id int64) (entity.Tweet, error) {
	tweet, ok := tr.storage.GetWithOK(id)
	if !ok {
		return tweet, ErrTweetNotFound
	}

	tr.storage.Delete(id)

	return tweet, nil
}

func (tr *TweetRepository) GetAllTweets() ([]entity.Tweet, error) {
	return tr.storage.GetAll(), nil
}

func (tr *TweetRepository) GetTweetByID(id int64) (entity.Tweet, error) {
	tweet, ok := tr.storage.GetWithOK(id)
	if !ok {
		return tweet, ErrTweetNotFound
	}

	return tweet, nil
}

func (tr *TweetRepository) UpdateTweet(tweet entity.Tweet) error {
	if !tr.storage.Has(tweet.ID) {
		return ErrTweetNotFound
	}

	tr.storage.Set(tweet.ID, tweet)

	return nil
}
