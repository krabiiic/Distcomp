package post

import (
	"context"

	"github.com/gocql/gocql"
	"github.com/stackus/errors"
	"lab3/discussion/internal/model"
)

var ErrNoticeNotFound = errors.Wrap(errors.ErrNotFound, "notice is not found")

func (n *PostRepo) GetPost(ctx context.Context, id int64) (model.Post, error) {
	const query = `SELECT id, topic_id, content FROM tbl_post WHERE id = ? LIMIT 1`

	var Post model.Post

	err := n.session.Query(query, id).
		WithContext(ctx).
		Scan(&Post.ID, &Post.TopicID, &Post.Content)
	if err != nil {
		if errors.Is(err, gocql.ErrNotFound) {
			return model.Post{}, ErrNoticeNotFound
		}

		return model.Post{}, err
	}

	return Post, nil
}

func (n *PostRepo) GetPosts(ctx context.Context) ([]model.Post, error) {
	const query = `SELECT id, topic_id, content FROM tbl_Post`

	Posts := make([]model.Post, 0)

	scanner := n.session.Query(query).WithContext(ctx).Iter().Scanner()

	for scanner.Next() {
		var Post model.Post

		err := scanner.Scan(&Post.ID, &Post.TopicID, &Post.Content)
		if err != nil {
			return nil, err
		}

		Posts = append(Posts, Post)
	}

	if _err := scanner.Err(); _err != nil {
		if errors.Is(_err, gocql.ErrNotFound) {
			return nil, ErrNoticeNotFound
		}

		return nil, _err
	}

	return Posts, nil
}

func (n *PostRepo) CreatePost(ctx context.Context, args model.Post) (model.Post, error) {
	const query = `INSERT INTO tbl_post (id, topic_id, content) VALUES (?, ?, ?)`

	Post := model.Post{
		ID:      n.nextID(),
		TopicID: args.TopicID,
		Content: args.Content,
	}

	err := n.session.Query(query, Post.ID, Post.TopicID, Post.Content).
		WithContext(ctx).
		Exec()
	if err != nil {
		return model.Post{}, err
	}

	return Post, nil
}

func (n *PostRepo) UpdatePost(ctx context.Context, args model.Post) (model.Post, error) {
	const query = `UPDATE tbl_post SET topic_id = ?, content = ? WHERE id = ?`

	_, err := n.GetPost(ctx, args.ID)
	if err != nil {
		return model.Post{}, err
	}

	err = n.session.Query(query, args.TopicID, args.Content, args.ID).WithContext(ctx).Exec()
	if err != nil {
		return model.Post{}, err
	}

	Post, err := n.GetPost(ctx, args.ID)
	if err != nil {
		return model.Post{}, err
	}

	return Post, nil
}

func (n *PostRepo) DeletePost(ctx context.Context, id int64) error {
	const query = `DELETE FROM tbl_post WHERE id = ?`

	_, err := n.GetPost(ctx, id)
	if err != nil {
		return err
	}

	err = n.session.Query(query, id).WithContext(ctx).Exec()
	if err != nil {
		return err
	}

	return nil
}
