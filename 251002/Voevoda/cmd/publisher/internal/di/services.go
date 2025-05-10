package di

import (
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type Services struct {
	User  UserService
	Label LabelService
	Tweet TweetService
}

type UserService interface {
	CreateUser(user entity.User) (entity.User, error)
	GetUserByID(id int64) (entity.User, error)
	GetAllUsers() ([]entity.User, error)
	UpdateUser(user entity.User) error
	DeleteUser(id int64) (entity.User, error)
}

type LabelService interface {
	CreateLabel(label entity.Label) (entity.Label, error)
	GetLabelByID(id int64) (entity.Label, error)
	GetAllLabels() ([]entity.Label, error)
	UpdateLabel(label entity.Label) error
	DeleteLabel(id int64) (entity.Label, error)
}

type TweetService interface {
	CreateTweet(tweet entity.Tweet) (entity.Tweet, error)
	GetTweetByID(id int64) (entity.Tweet, error)
	GetAllTweets() ([]entity.Tweet, error)
	UpdateTweet(tweet entity.Tweet) error
	DeleteTweet(id int64) (entity.Tweet, error)
}
