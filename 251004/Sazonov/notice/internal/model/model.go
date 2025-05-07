package model

type Notice struct {
	ID      int64  `db:"id"      json:"id"`
	NewsID  int64  `db:"news_id" json:"newsId"`
	Content string `db:"content" json:"content"`
}
