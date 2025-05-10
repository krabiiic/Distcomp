package psql

import (
	"context"
	"database/sql"
	"errors"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
	"github.com/strcarne/distributed-calculations/internal/entity"
)

var ErrLabelNotFound = errors.New("label not found")

type LabelRepository struct {
	queries *generated.Queries
}

func NewLabelRepository(queries *generated.Queries) *LabelRepository {
	if queries == nil {
		panic("queries is nil")
	}

	return &LabelRepository{
		queries: queries,
	}
}

func (repo *LabelRepository) GetAllLabels() ([]entity.Label, error) {
	labels, err := repo.queries.GetAllLabels(context.Background())
	if err != nil {
		return nil, err
	}

	convertedLabels := make([]entity.Label, 0, len(labels))
	for _, label := range labels {
		convertedLabels = append(convertedLabels, entity.Label{
			ID:   label.ID,
			Name: label.Name,
		})
	}

	return convertedLabels, nil
}

func (repo *LabelRepository) GetLabelByID(id int64) (entity.Label, error) {
	label, err := repo.queries.GetLabelByID(context.Background(), id)
	if err != nil {
		if err == sql.ErrNoRows {
			return entity.Label{}, ErrLabelNotFound
		}
		return entity.Label{}, err
	}
	return entity.Label{
		ID:   label.ID,
		Name: label.Name,
	}, nil
}

func (repo *LabelRepository) CreateLabel(label entity.Label) (int64, error) {
	createdLabel, err := repo.queries.CreateLabel(context.Background(), label.Name)
	if err != nil {
		return 0, err
	}

	return createdLabel.ID, nil
}

func (repo *LabelRepository) DeleteLabel(id int64) (entity.Label, error) {
	label, err := repo.GetLabelByID(id)
	if err != nil {
		return entity.Label{}, err
	}

	if err := repo.queries.DeleteLabel(context.Background(), id); err != nil {
		return entity.Label{}, err
	}

	return label, nil
}

func (repo *LabelRepository) UpdateLabel(label entity.Label) error {
	return repo.queries.UpdateLabel(context.Background(), generated.UpdateLabelParams{
		ID:   label.ID,
		Name: label.Name,
	})
}
