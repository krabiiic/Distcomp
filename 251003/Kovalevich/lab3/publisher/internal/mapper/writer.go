package mapper

import (
	writer "lab3/publisher/internal/model"
	"lab3/publisher/internal/storage/model"
)

func MapWriterToModel(cr writer.Writer) model.Writer {
	return model.Writer{
		ID:        cr.ID,
		Login:     cr.Login,
		Password:  cr.Password,
		FirstName: cr.FirstName,
		LastName:  cr.LastName,
	}
}

func MapModelToWriter(cr model.Writer) writer.Writer {
	return writer.Writer{
		ID:        cr.ID,
		Login:     cr.Login,
		Password:  cr.Password,
		FirstName: cr.FirstName,
		LastName:  cr.LastName,
	}
}
