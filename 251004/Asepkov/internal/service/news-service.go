package service

import (
	"RESTAPI/internal/dto"
	"RESTAPI/internal/entity"
	"RESTAPI/internal/repository"
	"RESTAPI/internal/storage"
	"context"
	"errors"
	"time"
)

type NewsService struct {
	newsRepo     repository.NewsRepository
	markRepo     repository.MarkRepository
	cacheService *CacheService
}

func NewNewsService(newsRepo repository.NewsRepository, markRepo repository.MarkRepository, cacheService *CacheService) *NewsService {
	return &NewsService{
		newsRepo:     newsRepo,
		markRepo:     markRepo,
		cacheService: cacheService,
	}
}

func (s *NewsService) Create(ctx context.Context, req dto.NewsRequestTo) (*dto.NewsResponseTo, error) {
	marks := []entity.Mark{}
	for _, markName := range req.Marks {
		// Try to find existing mark
		existingMarks, err := s.markRepo.GetByName(markName)

		var mark entity.Mark
		if err != nil || len(existingMarks) == 0 {
			// Create new mark if not found
			mark = entity.Mark{Name: markName}
			if err := s.markRepo.Create(&mark); err != nil {
				return nil, err
			}
		} else {
			mark = existingMarks[0]
		}

		marks = append(marks, mark)
	}

	// Check for duplicate title
	existingNews, err := s.newsRepo.GetAll()
	if err == nil { // Only check if we successfully got the news list
		for _, news := range existingNews {
			if news.Title == req.Title {
				return nil, errors.New("news with this title already exists")
			}
		}
	}

	news := &entity.News{
		WriterID: req.WriterID,
		Title:    req.Title,
		Content:  req.Content,
		Created:  time.Now(),
		Modified: time.Now(),
		Marks:    marks,
	}

	err = s.newsRepo.Create(news)
	if err != nil {
		return nil, err
	}

	// Invalidate cache for news list
	err = s.cacheService.InvalidateCache(ctx, "news:*")
	if err != nil {
		return nil, err
	}

	markResponses := make([]dto.MarkResponseTo, len(news.Marks))
	for i, mark := range news.Marks {
		markResponses[i] = dto.MarkResponseTo{
			ID:   mark.ID,
			Name: mark.Name,
		}
	}

	return &dto.NewsResponseTo{
		ID:       news.ID,
		WriterID: news.WriterID,
		Title:    news.Title,
		Content:  news.Content,
		Created:  news.Created,
		Modified: news.Modified,
		Marks:    markResponses,
	}, nil
}

func (s *NewsService) GetById(ctx context.Context, id int64) (*dto.NewsResponseTo, error) {
	var news entity.News
	cacheKey := storage.CreateCacheKey("news", id)

	// Try to get from cache first
	err := s.cacheService.GetOrSet(ctx, cacheKey, &news, func() (interface{}, error) {
		return s.newsRepo.GetById(id)
	}, 5*time.Minute)

	if err != nil {
		return nil, err
	}

	markResponses := make([]dto.MarkResponseTo, len(news.Marks))
	for i, mark := range news.Marks {
		markResponses[i] = dto.MarkResponseTo{
			ID:   mark.ID,
			Name: mark.Name,
		}
	}

	return &dto.NewsResponseTo{
		ID:       news.ID,
		WriterID: news.WriterID,
		Title:    news.Title,
		Content:  news.Content,
		Created:  news.Created,
		Modified: news.Modified,
		Marks:    markResponses,
	}, nil
}

func (s *NewsService) Update(ctx context.Context, req dto.NewsUpdateRequestTo) (*dto.NewsResponseTo, error) {
	news := &entity.News{
		WriterID: req.WriterID,
		Title:    req.Title,
		Content:  req.Content,
		ID:       req.ID,
	}
	err := s.newsRepo.Update(news)
	if err != nil {
		return nil, errors.New("failed to update news")
	}

	// Invalidate cache for this news and news list
	s.cacheService.Delete(ctx, storage.CreateCacheKey("news", news.ID))
	err = s.cacheService.InvalidateCache(ctx, "news:*")
	if err != nil {
		return nil, err
	}

	return &dto.NewsResponseTo{
		ID:       news.ID,
		WriterID: news.WriterID,
		Title:    news.Title,
		Content:  news.Content,
		Created:  news.Created,
		Modified: news.Modified,
	}, nil
}

func (s *NewsService) Delete(ctx context.Context, id int64) error {
	// First get the news with its marks to know which marks to potentially delete
	news, err := s.newsRepo.GetById(id)
	if err != nil {
		return err
	}

	// Extract mark names to delete them later if needed
	markNames := make([]string, len(news.Marks))
	for i, mark := range news.Marks {
		markNames[i] = mark.Name
	}

	// Delete the news with its mark associations
	err = s.newsRepo.Delete(id)
	if err != nil {
		return err
	}

	// Now delete the marks if they're no longer used
	err = s.markRepo.DeleteOrphaned()
	if err != nil {
		return err
	}

	// Invalidate cache for this news and news list
	s.cacheService.Delete(ctx, storage.CreateCacheKey("news", id))
	return s.cacheService.InvalidateCache(ctx, "news:*")
}

func (s *NewsService) GetAll(ctx context.Context) ([]*dto.NewsResponseTo, error) {
	var news []entity.News
	cacheKey := "news:all"

	// Try to get from cache first
	err := s.cacheService.GetOrSet(ctx, cacheKey, &news, func() (interface{}, error) {
		return s.newsRepo.GetAll()
	}, 5*time.Minute)

	if err != nil {
		return nil, err
	}

	response := make([]*dto.NewsResponseTo, len(news))
	for i, news := range news {
		response[i] = &dto.NewsResponseTo{
			ID:       news.ID,
			WriterID: news.WriterID,
			Title:    news.Title,
			Content:  news.Content,
			Created:  news.Created,
			Modified: news.Modified,
		}
	}
	return response, nil
}
