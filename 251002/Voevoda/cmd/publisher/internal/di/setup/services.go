package setup

import (
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/service"
)

func mustServices(queries *generated.Queries) di.Services {
	userRepository := psql.NewUserRepository(queries)
	userService := service.NewUser(userRepository)

	labelRepository := psql.NewLabelRepository(queries)
	labelService := service.NewLabel(labelRepository)

	tweetRepository := psql.NewTweetRepository(queries)
	tweetService := service.NewTweet(tweetRepository)

	return di.Services{
		User:  userService,
		Label: labelService,
		Tweet: tweetService,
	}
}
