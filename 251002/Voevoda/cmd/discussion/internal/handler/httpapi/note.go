package httpapi

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"

	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/di"

	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/internal/entity/server"
)

func NewNoteRouter(deps di.Container) *chi.Mux {
	r := chi.NewRouter()

	r.Route("/", func(r chi.Router) {
		r.Get("/", wrapHandler(GetNotes, deps))
		r.Get("/{id}", wrapHandler(GetNote, deps))
		r.Post("/", wrapHandler(CreateNote, deps))
		r.Delete("/{id}", wrapHandler(DeleteNote, deps))
		r.Put("/", wrapHandler(UpdateNote, deps))
	})

	return r
}

func GetNotes(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	notes, err := deps.Services.Note.GetAllNotes()
	if err != nil {
		return server.NewError(err, http.StatusInternalServerError)
	}

	json.NewEncoder(w).Encode(notes)

	return nil
}

func GetNote(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	note, err := deps.Services.Note.GetNoteByID(id)
	if err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	json.NewEncoder(w).Encode(note)

	return nil
}

func CreateNote(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var note entity.Note
	if err := json.NewDecoder(r.Body).Decode(&note); err != nil {
		return server.NewError(err, http.StatusForbidden)
	}

	note, err := deps.Services.Note.CreateNote(note)
	if err != nil {
		return server.NewError(err, http.StatusForbidden)
	}

	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(note)

	return nil
}

func DeleteNote(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	note, err := deps.Services.Note.DeleteNote(id)
	if err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	json.NewEncoder(w).Encode(note)

	return nil
}

func UpdateNote(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var note entity.Note
	if err := json.NewDecoder(r.Body).Decode(&note); err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	if err := deps.Services.Note.UpdateNote(note); err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	json.NewEncoder(w).Encode(note)

	return nil
}
