package notice

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	noticeModel "lab3/publisher/internal/model"
	noticeService "lab3/publisher/internal/service/notice"
)

type NoticeHandler struct {
	service noticeService.NoticeService
}

func New(srv noticeService.NoticeService) *NoticeHandler {
	return &NoticeHandler{
		service: srv,
	}
}

func (h *NoticeHandler) InitRoutes(r *mux.Router) {
	r.HandleFunc("/notices", h.getNoticesList).Methods(http.MethodGet)
	r.HandleFunc("/notices", h.createNotice).Methods(http.MethodPost)
	r.HandleFunc("/notices/{id}", h.getNoticeByID).Methods(http.MethodGet)
	r.HandleFunc("/notices/{id}", h.deleteNoticeByID).Methods(http.MethodDelete)
	r.HandleFunc("/notices", h.updateNoticeByID).Methods(http.MethodPut)
}

func (h *NoticeHandler) getNoticesList(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	notices, err := h.service.GetNotices(ctx)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to get notices")
		return
	}

	if notices == nil {
		notices = []noticeModel.Notice{}
	}

	respondWithJSON(w, http.StatusOK, notices)
}

func (h *NoticeHandler) getNoticeByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid ID format")
		return
	}

	ctx := r.Context()

	notice, err := h.service.GetNoticeByID(ctx, id)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to get Notice")
		return
	}

	respondWithJSON(w, http.StatusOK, notice)
}

func (h *NoticeHandler) createNotice(w http.ResponseWriter, r *http.Request) {
	var notice noticeModel.Notice
	if err := json.NewDecoder(r.Body).Decode(&notice); err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := notice.Validate(); err != nil {
		respondWithError(w, http.StatusBadRequest, err.Error())
		return
	}

	ctx := r.Context()

	createdNotice, err := h.service.CreateNotice(ctx, notice)
	if err != nil {
		fmt.Println(err.Error())
		respondWithError(w, http.StatusInternalServerError, "Failed to create Notice")
		return
	}

	respondWithJSON(w, http.StatusCreated, createdNotice)
}

func (h *NoticeHandler) deleteNoticeByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid ID format")
		return
	}

	ctx := r.Context()

	err = h.service.DeleteNoticeByID(ctx, id)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to delete Notice")
		return
	}

	respondWithJSON(w, http.StatusNoContent, map[string]string{"Notice": "Notice deleted successfully"})
}

func (h *NoticeHandler) updateNoticeByID(w http.ResponseWriter, r *http.Request) {
	var notice noticeModel.Notice
	if err := json.NewDecoder(r.Body).Decode(&notice); err != nil {
		respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := notice.Validate(); err != nil {
		respondWithError(w, http.StatusBadRequest, err.Error())
		return
	}

	ctx := r.Context()

	updatedNotice, err := h.service.UpdateNoticeByID(ctx, notice)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Failed to update Notice")
		return
	}

	respondWithJSON(w, http.StatusOK, updatedNotice)
}

func respondWithError(w http.ResponseWriter, code int, notice string) {
	respondWithJSON(w, code, map[string]string{"error": notice})
}

func respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	if payload != nil {
		json.NewEncoder(w).Encode(payload)
	}
}
