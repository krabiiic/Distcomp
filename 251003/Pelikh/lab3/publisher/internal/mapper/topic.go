package mapper

import (
	"time"

	topic "lab3/publisher/internal/model"
	"lab3/publisher/internal/storage/model"
)

func MapTopicToModel(i topic.Topic) (model.Topic, error) {
	var err error

	modified := time.Time{}
	if i.Modified != "" {
		modified, err = time.Parse(time.RFC3339, i.Modified)
		if err != nil {
			return model.Topic{}, err
		}
	}

	return model.Topic{
		ID:        i.ID,
		CreatorID: i.CreatorID,
		Title:     i.Title,
		Content:   i.Content,
		Modified:  &modified,
		Markers:   i.Markers,
	}, nil
}

func MapModelToTopic(i model.Topic) topic.Topic {
	var modified string

	if i.Modified != nil {
		modified = i.Modified.Format(time.RFC3339)
	}

	return topic.Topic{
		ID:        i.ID,
		CreatorID: i.CreatorID,
		Title:     i.Title,
		Content:   i.Content,
		Created:   i.Created.Format(time.RFC3339),
		Modified:  modified,
		//Markers:   i.Markers, //chacha
	}
}
