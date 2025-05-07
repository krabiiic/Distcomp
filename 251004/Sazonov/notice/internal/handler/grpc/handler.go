package grpc

import (
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/grpc/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/service"
	"google.golang.org/grpc"
)

type grpcRegistrar interface {
	Register(reg grpc.ServiceRegistrar)
}

type handler struct {
	notice grpcRegistrar
}

func New(service service.Service) *handler {
	return &handler{
		notice: notice.New(service),
	}
}

func (h *handler) Register(reg grpc.ServiceRegistrar) {
	h.notice.Register(reg)
}
