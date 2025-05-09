package Writer

import (
	"context"

	"lab3/publisher/internal/mapper"
	Writer "lab3/publisher/internal/model"
	WriterModel "lab3/publisher/internal/model"
	dbWriterModel "lab3/publisher/internal/storage/model"
)

type service struct {
	db WriterDB
}

type WriterDB interface {
	CreateWriter(ctx context.Context, cr dbWriterModel.Writer) (dbWriterModel.Writer, error)
	GetWriters(ctx context.Context) ([]dbWriterModel.Writer, error)
	GetWriterByID(ctx context.Context, id int64) (dbWriterModel.Writer, error)
	UpdateWriterByID(ctx context.Context, cr dbWriterModel.Writer) (dbWriterModel.Writer, error)
	DeleteWriterByID(ctx context.Context, id int64) error
}

type WriterService interface {
	CreateWriter(ctx context.Context, cr WriterModel.Writer) (Writer.Writer, error)
	GetWriters(ctx context.Context) ([]WriterModel.Writer, error)
	GetWriterByID(ctx context.Context, id int) (WriterModel.Writer, error)
	UpdateWriterByID(ctx context.Context, cr WriterModel.Writer) (WriterModel.Writer, error)
	DeleteWriterByID(ctx context.Context, id int) error
}

func New(db WriterDB) WriterService {
	return &service{
		db: db,
	}
}

func (s *service) CreateWriter(ctx context.Context, cr WriterModel.Writer) (Writer.Writer, error) {
	mappedData, err := s.db.CreateWriter(ctx, mapper.MapWriterToModel(cr))
	if err != nil {
		return Writer.Writer{}, err
	}

	return mapper.MapModelToWriter(mappedData), nil
}

func (s *service) GetWriters(ctx context.Context) ([]WriterModel.Writer, error) {
	Writers, err := s.db.GetWriters(ctx)
	if err != nil {
		return nil, err
	}

	var mappedWriters []WriterModel.Writer

	for _, Writer := range Writers {
		mappedWriters = append(mappedWriters, mapper.MapModelToWriter(Writer))
	}

	return mappedWriters, nil
}

func (s *service) GetWriterByID(ctx context.Context, id int) (WriterModel.Writer, error) {
	Writer, err := s.db.GetWriterByID(ctx, int64(id))
	if err != nil {
		return WriterModel.Writer{}, err
	}

	return mapper.MapModelToWriter(Writer), nil
}

func (s *service) UpdateWriterByID(ctx context.Context, cr WriterModel.Writer) (WriterModel.Writer, error) {
	Writer, err := s.db.UpdateWriterByID(ctx, mapper.MapWriterToModel(cr))
	if err != nil {
		return WriterModel.Writer{}, err
	}

	return mapper.MapModelToWriter(Writer), nil
}

func (s *service) DeleteWriterByID(ctx context.Context, id int) error {
	return s.db.DeleteWriterByID(ctx, int64(id))
}
