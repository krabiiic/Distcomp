package service

import (
	"errors"

	"github.com/strcarne/distributed-calculations/internal/entity"
)

var (
	ErrInvalidLogin    = errors.New("invalid login")
	ErrInvalidPassword = errors.New("invalid password")
)

type User struct {
	repo UserRepository
}

type UserRepository interface {
	CreateUser(user entity.User) (int64, error)
	DeleteUser(id int64) (entity.User, error)
	GetAllUsers() ([]entity.User, error)
	GetUserByID(id int64) (entity.User, error)
	UpdateUser(user entity.User) error
}

func NewUser(repo UserRepository) User {
	return User{
		repo: repo,
	}
}

func (u User) CreateUser(user entity.User) (entity.User, error) {
	id, err := u.repo.CreateUser(user)
	if err != nil {
		return entity.User{}, err
	}

	user.ID = id

	return user, nil
}

func (u User) DeleteUser(id int64) (entity.User, error) {
	user, err := u.repo.DeleteUser(id)
	if err != nil {
		return entity.User{}, err
	}

	return user, nil
}

func (u User) GetAllUsers() ([]entity.User, error) {
	users, err := u.repo.GetAllUsers()
	if err != nil {
		return nil, err
	}

	return users, nil
}

func (u User) GetUserByID(id int64) (entity.User, error) {
	user, err := u.repo.GetUserByID(id)
	if err != nil {
		return entity.User{}, err
	}

	return user, nil
}

func (u User) UpdateUser(user entity.User) error {
	if len(user.Login) < 2 {
		return ErrInvalidLogin
	}

	if len(user.Password) < 8 {
		return ErrInvalidPassword
	}

	err := u.repo.UpdateUser(user)
	if err != nil {
		return err
	}

	return nil
}
