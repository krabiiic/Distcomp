package service

import "lab3/discussion/internal/storage"

type service struct {
	PostService
}

func New(repo storage.Repository) Service {
	return service{
		PostService: repo,
	}
}
