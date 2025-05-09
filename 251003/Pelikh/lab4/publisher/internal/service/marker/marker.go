package marker

import (
	"context"
	"log"

	"lab3/publisher/internal/mapper"
	markerModel "lab3/publisher/internal/model"
	dbMarkerModel "lab3/publisher/internal/storage/model"
)

type service struct {
	db MarkerDB
}

type MarkerDB interface {
	CreateMarker(ctx context.Context, marker dbMarkerModel.Marker) (dbMarkerModel.Marker, error)
	GetMarkers(ctx context.Context) ([]dbMarkerModel.Marker, error)
	GetMarkerByID(ctx context.Context, id int64) (dbMarkerModel.Marker, error)
	UpdateMarkerByID(ctx context.Context, marker dbMarkerModel.Marker) (dbMarkerModel.Marker, error)
	DeleteMarkerByID(ctx context.Context, id int64) error
}

type MarkerService interface {
	CreateMarker(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error)
	GetMarkers(ctx context.Context) ([]markerModel.Marker, error)
	GetMarkerByID(ctx context.Context, id int64) (markerModel.Marker, error)
	UpdateMarkerByID(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error)
	DeleteMarkerByID(ctx context.Context, id int64) error
}

func New(db MarkerDB) MarkerService {
	return &service{
		db: db,
	}
}

func (s *service) CreateMarker(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error) {
	m, err := s.db.CreateMarker(ctx, mapper.MapMarkerToModel(marker))
	if err != nil {
		return markerModel.Marker{}, err
	}

	log.Println(m)

	return mapper.MapModelToMarker(m), nil
}

func (s *service) GetMarkers(ctx context.Context) ([]markerModel.Marker, error) {
	var mappedMarkers []markerModel.Marker

	markers, err := s.db.GetMarkers(ctx)
	if err != nil {
		return mappedMarkers, err
	}

	for _, m := range markers {
		mappedMarkers = append(mappedMarkers, mapper.MapModelToMarker(m))
	}

	if len(mappedMarkers) == 0 {
		return []markerModel.Marker{}, nil
	}

	return mappedMarkers, nil
}

func (s *service) GetMarkerByID(ctx context.Context, id int64) (markerModel.Marker, error) {
	marker, err := s.db.GetMarkerByID(ctx, id)
	if err != nil {
		return markerModel.Marker{}, err
	}

	return mapper.MapModelToMarker(marker), nil
}

func (s *service) UpdateMarkerByID(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error) {
	m, err := s.db.UpdateMarkerByID(ctx, mapper.MapMarkerToModel(marker))
	if err != nil {
		return markerModel.Marker{}, err
	}

	return mapper.MapModelToMarker(m), nil
}

func (s *service) DeleteMarkerByID(ctx context.Context, id int64) error {
	return s.db.DeleteMarkerByID(ctx, id)
}
