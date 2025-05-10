package post

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	PostModel "lab3/publisher/internal/model"
	PostService "lab3/publisher/internal/service/post"
)

type PostHandler struct {
	service PostService.PostService
}

func New(srv PostService.PostService) *PostHandler {
	return &PostHandler{
		service: srv,
	}
}

func (h *PostHandler) InitRoutes(r *mux.Router) {
	r.HandleFunc("/posts", h.getPostsList).Methods(http.MethodGet)
	r.HandleFunc("/posts", h.createPost).Methods(http.MethodPost)
	r.HandleFunc("/posts/{id}", h.getPostByID).Methods(http.MethodGet)
	r.HandleFunc("/posts/{id}", h.deletePostByID).Methods(http.MethodDelete)
	r.HandleFunc("/posts", h.updatePostByID).Methods(http.MethodPut)
}

func (h *PostHandler) getPostsList(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	Posts, err := h.service.GetPosts(ctx)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to get Posts")
		return
	}

	if Posts == nil {
		Posts = []PostModel.Post{}
	}

	respondWithJSON(w, http.StatusOK, Posts)
}

func (h *PostHandler) getPostByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid ID format")
		return
	}

	ctx := r.Context()

	Post, err := h.service.GetPostByID(ctx, id)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to get Post")
		return
	}

	respondWithJSON(w, http.StatusOK, Post)
}

func (h *PostHandler) createPost(w http.ResponseWriter, r *http.Request) {
	var msg PostModel.Post
	if err := json.NewDecoder(r.Body).Decode(&msg); err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := msg.Validate(); err != nil {
		respondWithError(w, http.StatusBadRequest, err.Error())
		return
	}

	ctx := r.Context()

	createdPost, err := h.service.CreatePost(ctx, msg)
	if err != nil {
		fmt.Println(err.Error())
		respondWithError(w, http.StatusInternalServerError, "Failed to create Post")
		return
	}

	respondWithJSON(w, http.StatusCreated, createdPost)
}

func (h *PostHandler) deletePostByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid ID format")
		return
	}

	ctx := r.Context()

	err = h.service.DeletePostByID(ctx, id)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to delete Post")
		return
	}

	respondWithJSON(w, http.StatusNoContent, map[string]string{"Post": "Post deleted successfully"})
}

func (h *PostHandler) updatePostByID(w http.ResponseWriter, r *http.Request) {
	var msg PostModel.Post
	if err := json.NewDecoder(r.Body).Decode(&msg); err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := msg.Validate(); err != nil {
		respondWithError(w, http.StatusBadRequest, err.Error())
		return
	}

	ctx := r.Context()

	updatedPost, err := h.service.UpdatePostByID(ctx, msg)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to update Post")
		return
	}

	respondWithJSON(w, http.StatusOK, updatedPost)
}

func respondWithError(w http.ResponseWriter, code int, Post string) {
	respondWithJSON(w, code, map[string]string{"error": Post})
}

func respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	if payload != nil {
		json.NewEncoder(w).Encode(payload)
	}
}
