package inmemory

import (
	"errors"
	"sync/atomic"

	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/pkg/container"
)

var ErrUserNotFound = errors.New("user not found")

type UserRepository struct {
	storage *container.InMemoryStorage[int64, entity.User]

	// counter is incremeted field that will be id of new userur.
	counter atomic.Int64
}

func NewUserRepository() *UserRepository {
	return &UserRepository{
		storage: container.DefaultInMemoryStorage[int64, entity.User](),
		counter: atomic.Int64{},
	}
}

func (ur *UserRepository) createNewID() int64 {
	return ur.counter.Add(1)
}

func (ur *UserRepository) CreateUser(user entity.User) (int64, error) {
	newID := ur.createNewID()
	user.ID = newID

	ur.storage.Set(newID, user)

	return newID, nil
}

func (ur *UserRepository) DeleteUser(id int64) (entity.User, error) {
	user, ok := ur.storage.GetWithOK(id)
	if !ok {
		return user, ErrUserNotFound
	}

	ur.storage.Delete(id)

	return user, nil
}

func (ur *UserRepository) GetAllUsers() ([]entity.User, error) {
	return ur.storage.GetAll(), nil
}

func (ur *UserRepository) GetUserByID(id int64) (entity.User, error) {
	user, ok := ur.storage.GetWithOK(id)
	if !ok {
		return user, ErrUserNotFound
	}

	return user, nil
}

func (ur *UserRepository) UpdateUser(user entity.User) error {
	if !ur.storage.Has(user.ID) {
		return ErrUserNotFound
	}

	ur.storage.Set(user.ID, user)

	return nil
}
