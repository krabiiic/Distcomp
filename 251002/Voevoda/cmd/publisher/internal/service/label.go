package service

import (
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type Label struct {
	repo LabelRepository
}

type LabelRepository interface {
	CreateLabel(label entity.Label) (int64, error)
	DeleteLabel(id int64) (entity.Label, error)
	GetAllLabels() ([]entity.Label, error)
	GetLabelByID(id int64) (entity.Label, error)
	UpdateLabel(label entity.Label) error
}

func NewLabel(repo LabelRepository) Label {
	return Label{
		repo: repo,
	}
}

func (u Label) CreateLabel(label entity.Label) (entity.Label, error) {
	id, err := u.repo.CreateLabel(label)
	if err != nil {
		return entity.Label{}, err
	}

	label.ID = id

	return label, nil
}

func (u Label) DeleteLabel(id int64) (entity.Label, error) {
	label, err := u.repo.DeleteLabel(id)
	if err != nil {
		return entity.Label{}, err
	}

	return label, nil
}

func (u Label) GetAllLabels() ([]entity.Label, error) {
	labels, err := u.repo.GetAllLabels()
	if err != nil {
		return nil, err
	}

	return labels, nil
}

func (u Label) GetLabelByID(id int64) (entity.Label, error) {
	label, err := u.repo.GetLabelByID(id)
	if err != nil {
		return entity.Label{}, err
	}

	return label, nil
}

func (u Label) UpdateLabel(label entity.Label) error {
	err := u.repo.UpdateLabel(label)
	if err != nil {
		return err
	}

	return nil
}
