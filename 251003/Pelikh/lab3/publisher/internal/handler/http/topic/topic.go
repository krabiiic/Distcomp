package topic

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"

	topicModel "lab3/publisher/internal/model"
	topicDbModel "lab3/publisher/internal/storage/psql/topic"

	"github.com/gorilla/mux"
)

type Topic struct {
	srv TopicService
}

func New(srv TopicService) Topic {
	return Topic{
		srv: srv,
	}
}

type TopicService interface {
	CreateTopic(ctx context.Context, topic topicModel.Topic) (topicModel.Topic, error)
	GetTopics(ctx context.Context) ([]topicModel.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (topicModel.Topic, error)
	UpdateTopicByID(ctx context.Context, topic topicModel.Topic) (topicModel.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

func (i Topic) InitRoutes(r *mux.Router) {
	r.HandleFunc("/topics", i.getTopicsList).Methods(http.MethodGet)
	r.HandleFunc("/topics", i.createTopic).Methods(http.MethodPost)
	r.HandleFunc("/topics/{id}", i.getTopicByID).Methods(http.MethodGet)
	r.HandleFunc("/topics/{id}", i.deleteTopicByID).Methods(http.MethodDelete)
	r.HandleFunc("/topics", i.updateTopicByID).Methods(http.MethodPut)
}

func (i Topic) getTopicsList(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	topics, err := i.srv.GetTopics(ctx)
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve Topics: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(topics); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode Topics: %v", err), http.StatusInternalServerError)
	}
}

func (i Topic) createTopic(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var topic topicModel.Topic

	if err := json.NewDecoder(r.Body).Decode(&topic); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := topic.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	createdTopic, err := i.srv.CreateTopic(ctx, topic)
	if err != nil {
		if errors.Is(err, topicDbModel.ErrInvalidForeignKey) {
			http.Error(w, "{}", http.StatusForbidden)
			return
		}

		if errors.Is(err, topicDbModel.ErrInvalidTopicData) {
			http.Error(w, "{}", http.StatusForbidden)
			return
		}

		http.Error(w, "{}", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	if err := json.NewEncoder(w).Encode(createdTopic); err != nil {
		http.Error(w, "{}", http.StatusInternalServerError)
	}
}

func (i Topic) getTopicByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	idStr := mux.Vars(r)["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	topic, err := i.srv.GetTopicByID(ctx, id)
	if err != nil {
		if err == topicDbModel.ErrTopicNotFound {
			http.Error(w, "Topic not found", http.StatusNotFound)
		} else {
			http.Error(w, fmt.Sprintf("failed to retrieve Topic: %v", err), http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(topic); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode Topic: %v", err), http.StatusInternalServerError)
	}
}

func (i Topic) deleteTopicByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	idStr := mux.Vars(r)["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid Topic ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := i.srv.DeleteTopicByID(ctx, id); err != nil {
		if err == topicDbModel.ErrTopicNotFound {
			http.Error(w, "{}", http.StatusNotFound)
		} else {
			http.Error(w, "{}", http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNoContent)
	w.Write([]byte(`{}`))
}

func (i Topic) updateTopicByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var topic topicModel.Topic

	if err := json.NewDecoder(r.Body).Decode(&topic); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := topic.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	updatedTopic, err := i.srv.UpdateTopicByID(ctx, topic)
	if err != nil {
		if err == topicDbModel.ErrTopicNotFound {
			http.Error(w, "{}", http.StatusNotFound)
		} else {
			http.Error(w, "{}", http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(updatedTopic); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode updated Topic: %v", err), http.StatusInternalServerError)
	}
}
