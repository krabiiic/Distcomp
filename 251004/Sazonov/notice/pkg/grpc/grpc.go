package grpc

import (
	"context"
	"errors"
	"fmt"
	"net"
	"time"

	"google.golang.org/grpc"
)

type Registerer interface{}

type Config struct {
	Host    string
	Port    string
	Timeout time.Duration
}

type grpcServer struct {
	*grpc.Server

	cfg Config
}

func NewServer(cfg Config) *grpcServer {
	server := grpc.NewServer()

	return &grpcServer{
		Server: server,

		cfg: cfg,
	}
}

func (gs *grpcServer) Run(ctx context.Context) error {
	const op = "run grpc server"

	listener, err := net.Listen("tcp", fmt.Sprintf("%s:%s", gs.cfg.Host, gs.cfg.Port))
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	if err := gs.Serve(listener); err != nil && !errors.Is(err, net.ErrClosed) {
		return fmt.Errorf("%s: %w", op, err)
	}

	return nil
}

func (gs *grpcServer) Shutdown(ctx context.Context) error {
	gs.GracefulStop()

	return nil
}
