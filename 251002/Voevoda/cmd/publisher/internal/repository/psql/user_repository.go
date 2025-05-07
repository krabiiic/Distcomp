package psql

import (
	"context"
	"database/sql"
	"errors"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
	"github.com/strcarne/distributed-calculations/internal/entity"
)

var ErrUserNotFound = errors.New("user not found")

type UserRepository struct {
	queries *generated.Queries
}

func NewUserRepository(queries *generated.Queries) *UserRepository {
	if queries == nil {
		panic("queries is nil")
	}

	return &UserRepository{
		queries: queries,
	}
}

func (repo *UserRepository) GetAllUsers() ([]entity.User, error) {
	users, err := repo.queries.GetAllUsers(context.Background())
	if err != nil {
		return nil, err
	}

	convertedUsers := make([]entity.User, 0, len(users))
	for _, user := range users {
		convertedUsers = append(convertedUsers, entity.User{
			ID:        user.ID,
			Login:     user.Login,
			Password:  user.Password,
			FirstName: user.Firstname,
			LastName:  user.Lastname,
		})
	}

	return convertedUsers, nil
}

func (repo *UserRepository) GetUserByID(id int64) (entity.User, error) {
	user, err := repo.queries.GetUserByID(context.Background(), id)
	if err != nil {
		if err == sql.ErrNoRows {
			return entity.User{}, ErrUserNotFound
		}
		return entity.User{}, err
	}
	return entity.User{
		ID:        user.ID,
		Login:     user.Login,
		Password:  user.Password,
		FirstName: user.Firstname,
		LastName:  user.Lastname,
	}, nil
}

func (repo *UserRepository) CreateUser(user entity.User) (int64, error) {
	createdUser, err := repo.queries.CreateUser(context.Background(), generated.CreateUserParams{
		Login:     user.Login,
		Password:  user.Password,
		Firstname: user.FirstName,
		Lastname:  user.LastName,
	})
	if err != nil {
		return 0, err
	}

	return createdUser.ID, nil
}

func (repo *UserRepository) DeleteUser(id int64) (entity.User, error) {
	user, err := repo.GetUserByID(id)
	if err != nil {
		return entity.User{}, err
	}

	if err := repo.queries.DeleteUser(context.Background(), id); err != nil {
		return entity.User{}, err
	}

	return user, nil
}

func (repo *UserRepository) UpdateUser(user entity.User) error {
	err := repo.queries.UpdateUser(context.Background(), generated.UpdateUserParams{
		ID:        user.ID,
		Login:     user.Login,
		Password:  user.Password,
		Firstname: user.FirstName,
		Lastname:  user.LastName,
	})
	if err != nil {
		return err
	}

	return nil
}
