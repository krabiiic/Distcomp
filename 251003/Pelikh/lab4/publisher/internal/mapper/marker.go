package mapper

import (
	marker "lab3/publisher/internal/model"
	"lab3/publisher/internal/storage/model"
)

func MapMarkerToModel(i marker.Marker) model.Marker {
	return model.Marker{
		ID:   int64(i.ID),
		Name: i.Name,
	}
}

func MapModelToMarker(i model.Marker) marker.Marker {
	return marker.Marker{
		ID:   i.ID,
		Name: i.Name,
	}
}
