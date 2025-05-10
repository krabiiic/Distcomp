package discussion

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"lab3/publisher/internal/model"
)

const (
	baseURL = "http://localhost:24130/api/v1.0"
)

type Client struct {
	httpClient *http.Client
}

type ClientInterface interface {
	GetNotices(ctx context.Context) ([]model.Notice, error)
	GetNotice(ctx context.Context, id int64) (*model.Notice, error)
	CreateNotice(ctx context.Context, topicID int64, content string) (*model.Notice, error)
	UpdateNotice(ctx context.Context, id, topicID int64, content string) (*model.Notice, error)
	DeleteNotice(ctx context.Context, id int64) error
}

func NewClient() ClientInterface {
	return &Client{
		httpClient: &http.Client{
			Timeout: 10 * time.Second,
		},
	}
}

func (c *Client) GetNotices(ctx context.Context) ([]model.Notice, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", baseURL+"/notices", nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	var notices []model.Notice
	if err := json.NewDecoder(resp.Body).Decode(&notices); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return notices, nil
}

func (c *Client) GetNotice(ctx context.Context, id int64) (*model.Notice, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", fmt.Sprintf("%s/notices/%d", baseURL, id), nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	var notice model.Notice
	if err := json.NewDecoder(resp.Body).Decode(&notice); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &notice, nil
}

func (c *Client) CreateNotice(ctx context.Context, topicID int64, content string) (*model.Notice, error) {
	notice := map[string]interface{}{
		"topicId": topicID,
		"content": content,
	}

	body, err := json.Marshal(notice)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal request body: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, "POST", baseURL+"/notices", bytes.NewBuffer(body))
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}
	req.Header.Set("Content-Type", "application/json")

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		body, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("unexpected status code: %d, body: %s", resp.StatusCode, string(body))
	}

	var createdNotice model.Notice
	if err := json.NewDecoder(resp.Body).Decode(&createdNotice); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &createdNotice, nil
}

func (c *Client) UpdateNotice(ctx context.Context, id, topicID int64, content string) (*model.Notice, error) {
	notice := map[string]interface{}{
		"id":      id,
		"topicId": topicID,
		"content": content,
	}

	body, err := json.Marshal(notice)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal request body: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, "PUT", baseURL+"/notices", bytes.NewBuffer(body))
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}
	req.Header.Set("Content-Type", "application/json")

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	var updatedNotice model.Notice
	if err := json.NewDecoder(resp.Body).Decode(&updatedNotice); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &updatedNotice, nil
}

func (c *Client) DeleteNotice(ctx context.Context, id int64) error {
	req, err := http.NewRequestWithContext(ctx, "DELETE", fmt.Sprintf("%s/notices/%d", baseURL, id), nil)
	if err != nil {
		return fmt.Errorf("failed to create request: %w", err)
	}

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusNoContent {
		return fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	return nil
}
