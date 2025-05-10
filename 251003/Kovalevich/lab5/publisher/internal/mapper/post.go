package mapper

import (
	postModel "lab3/publisher/internal/model"
)

func MapHTTPPostToModel(msg postModel.Post) postModel.Post {
	return postModel.Post{
		ID:      msg.ID,
		TopicID: msg.TopicID,
		Content: msg.Content,
	}
}

func MapModelToHTTPPost(msg postModel.Post) postModel.Post {
	return postModel.Post{
		ID:      msg.ID,
		TopicID: msg.TopicID,
		Content: msg.Content,
	}
}
