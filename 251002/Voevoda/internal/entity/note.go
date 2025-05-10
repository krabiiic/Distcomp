package entity

type Note struct {
	ID      int64  `json:"id,omitempty"`
	TweetID int    `json:"tweetId"`
	Content string `json:"content"`
	Country string `json:"country"`
}
