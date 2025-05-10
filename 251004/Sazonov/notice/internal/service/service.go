package service

import "github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/repository"

type service struct {
	NoticeService
}

func New(repo repository.Repository) Service {
	return service{
		NoticeService: repo,
	}
}
