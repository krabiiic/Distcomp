package news

import (
	"context"
	"database/sql"

	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/model"
	"github.com/Khmelov/Distcomp/251004/Sazonov/pkg/logger"
	"github.com/jackc/pgerrcode"
	"github.com/jackc/pgx/v5/pgconn"
	"github.com/jmoiron/sqlx"
	"github.com/stackus/errors"
	"go.uber.org/zap"
)

var (
	ErrNewsNotFound = errors.Wrap(errors.ErrNotFound, "news are not found")

	ErrNewsAlreadyExist = errors.Wrap(errors.ErrForbidden, "news already exist")

	ErrNoReference = errors.Wrap(errors.ErrNotFound, "writer is not found")

	ErrToAddNewsLabel = errors.Wrap(errors.ErrInternal, "add news label")
)

func (n *NewsRepo) GetNews(ctx context.Context, id int64) (model.News, error) {
	const query = `SELECT * FROM tbl_news WHERE id = $1 LIMIT 1`

	news := model.News{}
	if err := n.db.GetContext(ctx, &news, query, id); err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return model.News{}, ErrNewsNotFound
		}

		return model.News{}, err
	}

	return news, nil
}

func (n *NewsRepo) ListNews(ctx context.Context) ([]model.News, error) {
	const query = `SELECT * FROM tbl_news`

	news := []model.News{}
	if err := n.db.SelectContext(ctx, &news, query); err != nil {
		return nil, err
	}

	return news, nil
}

func (n *NewsRepo) CreateNews(ctx context.Context, args model.News) (model.News, error) {
	const query = `INSERT INTO tbl_news (
		writer_id, title, content
	) VALUES (
		$1, $2, $3
	) RETURNING *`

	tx, err := n.db.BeginTxx(ctx, &sql.TxOptions{})
	if err != nil {
		return model.News{}, err
	}
	defer tx.Rollback()

	rows, err := tx.QueryxContext(ctx, query, args.WriterID, args.Title, args.Content)
	if err != nil {
		var pgErr *pgconn.PgError

		if errors.As(err, &pgErr) && pgErr.Code == pgerrcode.UniqueViolation {
			return model.News{}, ErrNewsAlreadyExist
		}

		if errors.As(err, &pgErr) && pgErr.Code == pgerrcode.ForeignKeyViolation {
			return model.News{}, ErrNoReference
		}

		return model.News{}, err
	}
	defer rows.Close()

	var news model.News

	for rows.Next() {
		rows.StructScan(&news)
	}
	news.Labels = args.Labels

	if _err := rows.Err(); _err != nil {
		return model.News{}, _err
	}

	var labelErr error
	for _, label := range args.Labels {
		labelId, _err := addNewsLabel(tx, ctx, label)
		if _err != nil {
			labelErr = _err
			break
		}

		if _err := addNewsLabelRef(tx, ctx, news.ID, labelId); _err != nil {
			labelErr = _err
			break
		}
	}

	if labelErr != nil {
		logger.Error(ctx, "failed add label", zap.Error(labelErr))
		return model.News{}, nil
	}

	if err := tx.Commit(); err != nil {
		return model.News{}, err
	}

	return news, nil
}

func (n *NewsRepo) UpdateNews(ctx context.Context, args model.News) (model.News, error) {
	const query = `UPDATE tbl_news SET
		title	= COALESCE(NULLIF(:title, ''), title),
		content = COALESCE(NULLIF(:content, ''), content),
		writer_id = COALESCE(NULLIF(:writer_id, 0), writer_id),
		modified = NOW()
	WHERE id = :id
	RETURNING *`

	rows, err := n.db.NamedQueryContext(ctx, query, args)
	if err != nil {
		var pgErr *pgconn.PgError

		if errors.As(err, &pgErr) && pgErr.Code == pgerrcode.UniqueViolation {
			return model.News{}, ErrNewsAlreadyExist
		}

		if errors.As(err, &pgErr) && pgErr.Code == pgerrcode.ForeignKeyViolation {
			return model.News{}, ErrNoReference
		}

		return model.News{}, err
	}
	defer rows.Close()

	if !rows.Next() {
		return model.News{}, ErrNewsNotFound
	}

	var news model.News
	if err := rows.StructScan(&news); err != nil {
		return model.News{}, err
	}

	if err := rows.Err(); err != nil {
		return model.News{}, err
	}

	return news, nil
}

func (n *NewsRepo) DeleteNews(ctx context.Context, id int64) error {
	const query = `DELETE FROM tbl_news WHERE id = $1`

	result, err := n.db.ExecContext(ctx, query, id)
	if err != nil {
		logger.Error(ctx, "failed to delete news", zap.Error(err))
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}

	if rowsAffected != 1 {
		return ErrNewsNotFound
	}

	return nil
}

func addNewsLabelRef(tx *sqlx.Tx, ctx context.Context, newsId int64, labelId int64) error {
	const query = `INSERT INTO m2m_news_labels (news_id, label_id)
	VALUES ($1, $2)
	ON CONFLICT DO NOTHING`

	_, err := tx.ExecContext(ctx, query, newsId, labelId)
	if err != nil {
		return err
	}

	return nil
}

func addNewsLabel(tx *sqlx.Tx, ctx context.Context, label string) (int64, error) {
	const query = `INSERT INTO tbl_label (name) 
	VALUES ($1) 
	ON CONFLICT DO NOTHING 
	RETURNING id`

	rows, err := tx.QueryContext(ctx, query, label)
	if err != nil {
		return 0, err
	}
	defer rows.Close()

	if !rows.Next() {
		return 0, err
	}

	var id int64
	if err := rows.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}
