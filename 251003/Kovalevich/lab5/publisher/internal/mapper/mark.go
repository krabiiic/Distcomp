package mapper

import (
	mark "lab3/publisher/internal/model"
	"lab3/publisher/internal/storage/model"
)

func MapMarkToModel(i mark.Mark) model.Mark {
	return model.Mark{
		ID:   int64(i.ID),
		Name: i.Name,
	}
}

func MapModelToMark(i model.Mark) mark.Mark {
	return mark.Mark{
		ID:   i.ID,
		Name: i.Name,
	}
}
