package main

import (
	"context"
	"log"
	"os"

	"lab3/discussion/internal/app"
	"lab3/discussion/internal/service"
	"lab3/discussion/internal/storage"
	"lab3/discussion/pkg/cassandra"

	"github.com/joho/godotenv"
)

func init() {
	if err := godotenv.Load(); err != nil {
		log.Fatalf("couldn't read .env file: %v", err)
	}
}

func main() {
	app := app.New()

	db, err := storage.New(
		cassandra.Config{
			Addrs:    []string{os.Getenv("CASSANDRA_ADDRS")},
			Keyspace: os.Getenv("CASSANDRA_KEYSPACE"),
			User:     os.Getenv("CASSANDRA_USER"),
			Password: os.Getenv("CASSANDRA_PASSWORD"),
		},
	)
	if err != nil {
		log.Fatalf("couldn't connect to cassandra db: %v", err)
	}

	srv := service.New(db)

	if err := app.Start(context.Background(), srv); err != nil {
		log.Fatalf("couldn't start server")
	}
}
