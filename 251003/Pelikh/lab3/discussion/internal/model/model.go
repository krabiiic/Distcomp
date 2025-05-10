package model

type Notice struct {
	ID      int64  `db:"id"      json:"id"`
	TopicID int64  `db:"topic_id" json:"topicId"`
	Content string `db:"content" json:"content"`
}
