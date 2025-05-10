package notice

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/model"
	"github.com/stackus/errors"
)

var ErrNoticeNotFound = errors.Wrap(errors.ErrNotFound, "notice is not found")

func (n *Adapter) GetNotice(ctx context.Context, id int64) (model.Notice, error) {
	resp, err := n.cli.R().
		SetContext(ctx).
		SetPathParam("id", fmt.Sprint(id)).
		Get("/v1.0/notices/{id}")
	if err != nil {
		return model.Notice{}, nil
	}
	defer resp.Body.Close()

	var notice model.Notice

	if _err := json.NewDecoder(resp.Body).Decode(&notice); _err != nil {
		return model.Notice{}, _err
	}

	return notice, nil
}

func (n *Adapter) ListNotices(ctx context.Context) ([]model.Notice, error) {
	resp, err := n.cli.R().SetContext(ctx).Get("/v1.0/notices")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var notices []model.Notice

	if _err := json.NewDecoder(resp.Body).Decode(&notices); _err != nil {
		return nil, _err
	}

	return notices, nil
}

func (n *Adapter) CreateNotice(ctx context.Context, args model.Notice) (model.Notice, error) {
	data, err := json.Marshal(args)
	if err != nil {
		return model.Notice{}, err
	}

	resp, err := n.cli.R().
		SetContext(ctx).
		SetContentType("application/json").
		SetContentLength(true).
		SetBody(data).
		Post("/v1.0/notices")
	if err != nil {
		return model.Notice{}, err
	}
	defer resp.Body.Close()

	var notice model.Notice

	if _err := json.NewDecoder(resp.Body).Decode(&notice); _err != nil {
		return model.Notice{}, _err
	}

	return notice, nil
}

func (n *Adapter) UpdateNotice(ctx context.Context, args model.Notice) (model.Notice, error) {
	data, err := json.Marshal(args)
	if err != nil {
		return model.Notice{}, err
	}

	resp, err := n.cli.R().
		SetContext(ctx).
		SetContentType("application/json").
		SetContentLength(true).
		SetBody(data).
		Put("/v1.0/notices")
	if err != nil {
		return model.Notice{}, err
	}
	defer resp.Body.Close()

	var notice model.Notice

	if _err := json.NewDecoder(resp.Body).Decode(&notice); _err != nil {
		return model.Notice{}, _err
	}

	return notice, nil
}

func (n *Adapter) DeleteNotice(ctx context.Context, id int64) error {
	resp, err := n.cli.R().
		SetContext(ctx).
		SetPathParam("id", fmt.Sprint(id)).
		Delete("/v1.0/notices/{id}")
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode() == http.StatusNotFound {
		return ErrNoticeNotFound
	}

	return nil
}
