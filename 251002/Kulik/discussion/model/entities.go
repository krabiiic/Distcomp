package model

type Note struct {
	Id      int64  `bson:"id,omitempty"`
	StoryID int64  `bson:"story_id"`
	Content string `bson:"content"`
}
