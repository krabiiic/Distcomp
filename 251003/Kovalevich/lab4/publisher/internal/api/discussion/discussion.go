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
	GetPosts(ctx context.Context) ([]model.Post, error)
	GetPost(ctx context.Context, id int64) (*model.Post, error)
	CreatePost(ctx context.Context, topicID int64, content string) (*model.Post, error)
	UpdatePost(ctx context.Context, id, topicID int64, content string) (*model.Post, error)
	DeletePost(ctx context.Context, id int64) error
}

func NewClient() ClientInterface {
	return &Client{
		httpClient: &http.Client{
			Timeout: 10 * time.Second,
		},
	}
}

func (c *Client) GetPosts(ctx context.Context) ([]model.Post, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", baseURL+"/posts", nil)
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

	var posts []model.Post
	if err := json.NewDecoder(resp.Body).Decode(&posts); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return posts, nil
}

func (c *Client) GetPost(ctx context.Context, id int64) (*model.Post, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", fmt.Sprintf("%s/posts/%d", baseURL, id), nil)
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

	var post model.Post
	if err := json.NewDecoder(resp.Body).Decode(&post); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &post, nil
}

func (c *Client) CreatePost(ctx context.Context, topicID int64, content string) (*model.Post, error) {
	post := map[string]interface{}{
		"topicId": topicID,
		"content": content,
	}

	body, err := json.Marshal(post)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal request body: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, "POST", baseURL+"/posts", bytes.NewBuffer(body))
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

	var createdpost model.Post
	if err := json.NewDecoder(resp.Body).Decode(&createdpost); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &createdpost, nil
}

func (c *Client) UpdatePost(ctx context.Context, id, topicID int64, content string) (*model.Post, error) {
	post := map[string]interface{}{
		"id":      id,
		"topicId": topicID,
		"content": content,
	}

	body, err := json.Marshal(post)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal request body: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, "PUT", baseURL+"/posts", bytes.NewBuffer(body))
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

	var updatedpost model.Post
	if err := json.NewDecoder(resp.Body).Decode(&updatedpost); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return &updatedpost, nil
}

func (c *Client) DeletePost(ctx context.Context, id int64) error {
	req, err := http.NewRequestWithContext(ctx, "DELETE", fmt.Sprintf("%s/posts/%d", baseURL, id), nil)
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
