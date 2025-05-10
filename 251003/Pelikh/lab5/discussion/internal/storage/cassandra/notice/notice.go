package notice

import (
	"context"

	"github.com/gocql/gocql"
	"github.com/stackus/errors"
	"lab3/discussion/internal/model"
)

var ErrNoticeNotFound = errors.Wrap(errors.ErrNotFound, "notice is not found")

func (n *NoticeRepo) GetNotice(ctx context.Context, id int64) (model.Notice, error) {
	const query = `SELECT id, topic_id, content FROM tbl_notice WHERE id = ? LIMIT 1`

	var notice model.Notice

	err := n.session.Query(query, id).
		WithContext(ctx).
		Scan(&notice.ID, &notice.TopicID, &notice.Content)
	if err != nil {
		if errors.Is(err, gocql.ErrNotFound) {
			return model.Notice{}, ErrNoticeNotFound
		}

		return model.Notice{}, err
	}

	return notice, nil
}

func (n *NoticeRepo) GetNotices(ctx context.Context) ([]model.Notice, error) {
	const query = `SELECT id, topic_id, content FROM tbl_notice`

	notices := make([]model.Notice, 0)

	scanner := n.session.Query(query).WithContext(ctx).Iter().Scanner()

	for scanner.Next() {
		var notice model.Notice

		err := scanner.Scan(&notice.ID, &notice.TopicID, &notice.Content)
		if err != nil {
			return nil, err
		}

		notices = append(notices, notice)
	}

	if _err := scanner.Err(); _err != nil {
		if errors.Is(_err, gocql.ErrNotFound) {
			return nil, ErrNoticeNotFound
		}

		return nil, _err
	}

	return notices, nil
}

func (n *NoticeRepo) CreateNotice(ctx context.Context, args model.Notice) (model.Notice, error) {
	const query = `INSERT INTO tbl_notice (id, topic_id, content) VALUES (?, ?, ?)`

	notice := model.Notice{
		ID:      n.nextID(),
		TopicID: args.TopicID,
		Content: args.Content,
	}

	err := n.session.Query(query, notice.ID, notice.TopicID, notice.Content).
		WithContext(ctx).
		Exec()
	if err != nil {
		return model.Notice{}, err
	}

	return notice, nil
}

func (n *NoticeRepo) UpdateNotice(ctx context.Context, args model.Notice) (model.Notice, error) {
	const query = `UPDATE tbl_notice SET topic_id = ?, content = ? WHERE id = ?`

	_, err := n.GetNotice(ctx, args.ID)
	if err != nil {
		return model.Notice{}, err
	}

	err = n.session.Query(query, args.TopicID, args.Content, args.ID).WithContext(ctx).Exec()
	if err != nil {
		return model.Notice{}, err
	}

	notice, err := n.GetNotice(ctx, args.ID)
	if err != nil {
		return model.Notice{}, err
	}

	return notice, nil
}

func (n *NoticeRepo) DeleteNotice(ctx context.Context, id int64) error {
	const query = `DELETE FROM tbl_notice WHERE id = ?`

	_, err := n.GetNotice(ctx, id)
	if err != nil {
		return err
	}

	err = n.session.Query(query, id).WithContext(ctx).Exec()
	if err != nil {
		return err
	}

	return nil
}
