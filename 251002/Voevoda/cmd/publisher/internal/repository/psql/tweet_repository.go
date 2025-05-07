package psql

import (
	"context"
	"database/sql"
	"errors"

	"github.com/jackc/pgx/v5/pgtype"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
	"github.com/strcarne/distributed-calculations/internal/entity"
)

var ErrTweetNotFound = errors.New("tweet not found")

type TweetRepository struct {
	queries *generated.Queries
}

func NewTweetRepository(queries *generated.Queries) *TweetRepository {
	if queries == nil {
		panic("queries is nil")
	}

	return &TweetRepository{
		queries: queries,
	}
}

func (repo *TweetRepository) GetAllTweets() ([]entity.Tweet, error) {
	tweets, err := repo.queries.GetAllTweets(context.Background())
	if err != nil {
		return nil, err
	}

	convertedTweets := make([]entity.Tweet, 0, len(tweets))
	for _, tweet := range tweets {
		convertedTweets = append(convertedTweets, entity.Tweet{
			ID:       tweet.ID,
			UserID:   int(*tweet.UserID),
			Title:    tweet.Title,
			Content:  tweet.Content,
			Created:  tweet.Created.Time,
			Modified: tweet.Modified.Time,
		})
	}

	return convertedTweets, nil
}

func (repo *TweetRepository) GetTweetByID(id int64) (entity.Tweet, error) {
	tweet, err := repo.queries.GetTweetByID(context.Background(), id)
	if err != nil {
		if err == sql.ErrNoRows {
			return entity.Tweet{}, ErrTweetNotFound
		}
		return entity.Tweet{}, err
	}
	return entity.Tweet{
		ID:       tweet.ID,
		UserID:   int(*tweet.UserID),
		Title:    tweet.Title,
		Content:  tweet.Content,
		Created:  tweet.Created.Time,
		Modified: tweet.Modified.Time,
	}, nil
}

func (repo *TweetRepository) CreateTweet(tweet entity.Tweet) (int64, error) {
	userID := int64(tweet.UserID)

	createdTweet, err := repo.queries.CreateTweet(context.Background(), generated.CreateTweetParams{
		UserID:   &userID,
		Title:    tweet.Title,
		Content:  tweet.Content,
		Created:  pgtype.Timestamp{Time: tweet.Created, Valid: true},
		Modified: pgtype.Timestamp{Time: tweet.Modified, Valid: true},
	})
	if err != nil {
		return 0, err
	}

	return createdTweet.ID, nil
}

func (repo *TweetRepository) DeleteTweet(id int64) (entity.Tweet, error) {
	tweet, err := repo.GetTweetByID(id)
	if err != nil {
		return entity.Tweet{}, err
	}

	if err := repo.queries.DeleteTweet(context.Background(), id); err != nil {
		return entity.Tweet{}, err
	}

	return tweet, nil
}

func (repo *TweetRepository) UpdateTweet(tweet entity.Tweet) error {
	userID := int64(tweet.UserID)
	return repo.queries.UpdateTweet(context.Background(), generated.UpdateTweetParams{
		ID:       tweet.ID,
		UserID:   &userID,
		Title:    tweet.Title,
		Content:  tweet.Content,
		Modified: pgtype.Timestamp{Time: tweet.Modified, Valid: true},
	})
}
