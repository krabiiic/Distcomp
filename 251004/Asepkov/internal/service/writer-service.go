package service

import (
	"RESTAPI/internal/dto"
	"RESTAPI/internal/entity"
	"RESTAPI/internal/repository"
	"RESTAPI/internal/storage"
	"context"
	"fmt"
	"time"
)

type WriterService struct {
	repo         repository.WriterRepository
	cacheService *CacheService
}

func NewWriterService(repo repository.WriterRepository, cacheService *CacheService) *WriterService {
	return &WriterService{
		repo:         repo,
		cacheService: cacheService,
	}
}

// Create creates a new writer
func (s *WriterService) Create(ctx context.Context, req dto.WriterRequestTo) (*dto.WriterResponseTo, error) {
	// Check if the login already exists
	existingWriter, err := s.repo.GetByLogin(req.Login)
	if err == nil && existingWriter != nil {
		return nil, fmt.Errorf("login_already_exists")
	}

	writer := &entity.Writer{
		Login:     req.Login,
		Password:  req.Password,
		FirstName: req.FirstName,
		LastName:  req.LastName,
	}

	err = s.repo.Create(writer)
	if err != nil {
		return nil, err
	}

	// Invalidate cache for writers list
	err = s.cacheService.InvalidateCache(ctx, "writers:*")
	if err != nil {
		return nil, err
	}

	return &dto.WriterResponseTo{
		ID:        writer.ID,
		Login:     writer.Login,
		FirstName: writer.FirstName,
		LastName:  writer.LastName,
	}, nil
}

// GetById gets a writer by ID
func (s *WriterService) GetById(ctx context.Context, id int64) (*dto.WriterResponseTo, error) {
	if id < 0 {
		return nil, fmt.Errorf("invalid ID: %d", id)
	}

	var writer entity.Writer
	cacheKey := storage.CreateCacheKey("writer", id)

	// Try to get from cache first
	err := s.cacheService.GetOrSet(ctx, cacheKey, &writer, func() (interface{}, error) {
		return s.repo.GetById(id)
	}, 5*time.Minute)

	if err != nil {
		return nil, err
	}

	return &dto.WriterResponseTo{
		ID:        writer.ID,
		Login:     writer.Login,
		FirstName: writer.FirstName,
		LastName:  writer.LastName,
	}, nil
}

// Update updates a writer
func (s *WriterService) Update(ctx context.Context, req dto.WriterUpdateRequestTo) (*dto.WriterResponseTo, error) {
	writer := &entity.Writer{
		Login:     req.Login,
		Password:  req.Password,
		FirstName: req.FirstName,
		LastName:  req.LastName,
		ID:        req.ID,
	}

	err := s.repo.Update(writer)
	if err != nil {
		return nil, err
	}

	// Invalidate cache for this writer and writers list
	s.cacheService.Delete(ctx, storage.CreateCacheKey("writer", writer.ID))
	err = s.cacheService.InvalidateCache(ctx, "writers:*")
	if err != nil {
		return nil, err
	}

	return &dto.WriterResponseTo{
		ID:        writer.ID,
		Login:     writer.Login,
		FirstName: writer.FirstName,
		LastName:  writer.LastName,
	}, nil
}

// Delete deletes a writer
func (s *WriterService) Delete(ctx context.Context, id int64) error {
	err := s.repo.Delete(id)
	if err != nil {
		return err
	}

	// Invalidate cache for this writer and writers list
	s.cacheService.Delete(ctx, storage.CreateCacheKey("writer", id))
	err = s.cacheService.InvalidateCache(ctx, "writers:*")
	if err != nil {
		return err
	}

	return nil
}

// GetAll returns all writers
func (s *WriterService) GetAll(ctx context.Context) ([]*dto.WriterResponseTo, error) {
	var writers []entity.Writer
	cacheKey := "writers:all"

	// Try to get from cache first
	err := s.cacheService.GetOrSet(ctx, cacheKey, &writers, func() (interface{}, error) {
		return s.repo.GetAll()
	}, 5*time.Minute)

	if err != nil {
		return nil, err
	}

	response := make([]*dto.WriterResponseTo, len(writers))
	for i, writer := range writers {
		response[i] = &dto.WriterResponseTo{
			ID:        writer.ID,
			Login:     writer.Login,
			FirstName: writer.FirstName,
			LastName:  writer.LastName,
		}
	}
	return response, nil
}
