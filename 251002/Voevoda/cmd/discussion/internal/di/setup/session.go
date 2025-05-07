package setup

import (
	"log"

	"github.com/gocql/gocql"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/config"
)

func mustSession(cfg config.Config) *gocql.Session {
	cluster := gocql.NewCluster(cfg.Cassandra.Hosts...)
	cluster.Keyspace = cfg.Cassandra.Keyspace
	cluster.Consistency = gocql.ParseConsistency(cfg.Cassandra.Consistency)

	if cfg.Cassandra.Username != "" && cfg.Cassandra.Password != "" {
		cluster.Authenticator = gocql.PasswordAuthenticator{
			Username: cfg.Cassandra.Username,
			Password: cfg.Cassandra.Password,
		}
	}

	session, err := cluster.CreateSession()
	if err != nil {
		log.Panicf("Failed to create Cassandra session: %v", err)
	}

	return session
}
