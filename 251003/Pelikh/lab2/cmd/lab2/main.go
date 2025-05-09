package main

import (
	"context"
	"lab2/internal/app"
	"log"
	"os/signal"
	"syscall"

	"github.com/spf13/viper"
)

func init() {
	viper.AddConfigPath("config")
	viper.SetConfigName("config")

	viper.SetConfigType("yaml")
	if err := viper.ReadInConfig(); err != nil {
		log.Fatal(err)
	}
}

func main() {
	ctx, cancel := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer cancel()

	application, err := app.New()
	if err != nil {
		log.Fatal(err)
	}

	if err := application.Start(ctx); err != nil {
		log.Fatal(err)
	}
}
