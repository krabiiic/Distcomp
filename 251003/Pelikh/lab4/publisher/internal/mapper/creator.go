package mapper

import (
	creator "lab3/publisher/internal/model"
	"lab3/publisher/internal/storage/model"
)

func MapCreatorToModel(cr creator.Creator) model.Creator {
	return model.Creator{
		ID:        cr.ID,
		Login:     cr.Login,
		Password:  cr.Password,
		FirstName: cr.FirstName,
		LastName:  cr.LastName,
	}
}

func MapModelToCreator(cr model.Creator) creator.Creator {
	return creator.Creator{
		ID:        cr.ID,
		Login:     cr.Login,
		Password:  cr.Password,
		FirstName: cr.FirstName,
		LastName:  cr.LastName,
	}
}
