package setup

import (
	"log/slog"

	"github.com/gocql/gocql"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/di"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/repository/cassandra"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/service"
)

func mustServices(session *gocql.Session, logger *slog.Logger) di.Services {
	noteRepository := cassandra.NewNoteRepository(session, logger)
	noteService := service.NewNote(noteRepository)

	return di.Services{
		Note: noteService,
	}
}
