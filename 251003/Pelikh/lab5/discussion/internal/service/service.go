package service

import "lab3/discussion/internal/storage"

type service struct {
	NoticeService
}

func New(repo storage.Repository) Service {
	return service{
		NoticeService: repo,
	}
}
