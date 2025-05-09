package Notice

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/segmentio/kafka-go"
	"time"

	NoticeModel "lab3/publisher/internal/model"
)

type KafkaClient struct {
	producer       *kafka.Writer
	responseTopic  string
	responseReader *kafka.Reader
}

func NewKafkaClient(brokers []string, requestTopic, responseTopic string) *KafkaClient {
	return &KafkaClient{
		producer: &kafka.Writer{
			Addr:     kafka.TCP(brokers...),
			Topic:    requestTopic,
			Balancer: &kafka.LeastBytes{},
		},
		responseTopic: responseTopic,
		responseReader: kafka.NewReader(kafka.ReaderConfig{
			Brokers:   brokers,
			Topic:     responseTopic,
			Partition: 0,
			MinBytes:  10e3,
			MaxBytes:  10e6,
		}),
	}
}

type kafkaRequest struct {
	Action    string `json:"action"`
	ID        int64  `json:"id,omitempty"`
	topicID   int64  `json:"topic_id,omitempty"`
	Content   string `json:"content,omitempty"`
	ReplyTo   string `json:"reply_to"`
	Timestamp int64  `json:"timestamp"`
}

type kafkaResponse struct {
	Notice  *NoticeModel.Notice  `json:"Notice,omitempty"`
	Notices []NoticeModel.Notice `json:"Notices,omitempty"`
	Error   string               `json:"error,omitempty"`
}

func (k *KafkaClient) CreateNotice(ctx context.Context, topicID int64, content string) (*NoticeModel.Notice, error) {
	req := kafkaRequest{
		Action:    "create",
		topicID:   topicID,
		Content:   content,
		ReplyTo:   k.responseTopic,
		Timestamp: time.Now().Unix(),
	}

	if err := k.sendRequest(ctx, req); err != nil {
		return nil, err
	}

	resp, err := k.waitForResponse(ctx)
	if err != nil {
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf(resp.Error)
	}

	return resp.Notice, nil
}

func (k *KafkaClient) GetNotices(ctx context.Context) ([]NoticeModel.Notice, error) {
	req := kafkaRequest{
		Action:    "get_all",
		ReplyTo:   k.responseTopic,
		Timestamp: time.Now().Unix(),
	}

	if err := k.sendRequest(ctx, req); err != nil {
		return nil, err
	}

	resp, err := k.waitForResponse(ctx)
	if err != nil {
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf(resp.Error)
	}

	return resp.Notices, nil
}

func (k *KafkaClient) GetNotice(ctx context.Context, id int64) (*NoticeModel.Notice, error) {
	req := kafkaRequest{
		Action:    "get",
		ID:        id,
		ReplyTo:   k.responseTopic,
		Timestamp: time.Now().Unix(),
	}

	if err := k.sendRequest(ctx, req); err != nil {
		return nil, err
	}

	resp, err := k.waitForResponse(ctx)
	if err != nil {
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf(resp.Error)
	}

	return resp.Notice, nil
}

func (k *KafkaClient) UpdateNotice(ctx context.Context, id, topicID int64, content string) (*NoticeModel.Notice, error) {
	req := kafkaRequest{
		Action:    "update",
		ID:        id,
		topicID:   topicID,
		Content:   content,
		ReplyTo:   k.responseTopic,
		Timestamp: time.Now().Unix(),
	}

	if err := k.sendRequest(ctx, req); err != nil {
		return nil, err
	}

	resp, err := k.waitForResponse(ctx)
	if err != nil {
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf(resp.Error)
	}

	return resp.Notice, nil
}

func (k *KafkaClient) DeleteNotice(ctx context.Context, id int64) error {
	req := kafkaRequest{
		Action:    "delete",
		ID:        id,
		ReplyTo:   k.responseTopic,
		Timestamp: time.Now().Unix(),
	}

	if err := k.sendRequest(ctx, req); err != nil {
		return err
	}

	resp, err := k.waitForResponse(ctx)
	if err != nil {
		return err
	}

	if resp.Error != "" {
		return fmt.Errorf(resp.Error)
	}

	return nil
}

func (k *KafkaClient) sendRequest(ctx context.Context, req kafkaRequest) error {
	msgBytes, _ := json.Marshal(req)

	return k.producer.WriteMessages(ctx, kafka.Message{
		Key:   []byte(fmt.Sprintf("topic-%d", req.topicID)),
		Value: msgBytes,
	})
}

func (k *KafkaClient) waitForResponse(ctx context.Context) (*kafkaResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	for {
		select {
		case <-ctx.Done():
			return nil, ctx.Err()
		default:
			m, err := k.responseReader.ReadMessage(ctx)
			if err != nil {
				return nil, fmt.Errorf("failed to read response: %w", err)
			}

			var resp kafkaResponse
			if err := json.Unmarshal(m.Value, &resp); err != nil {
				continue
			}

			return &resp, nil
		}
	}
}

func mustMarshal(v interface{}) []byte {
	b, err := json.Marshal(v)
	if err != nil {
		panic(err)
	}
	return b
}
