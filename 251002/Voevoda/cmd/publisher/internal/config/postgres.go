package config

type Postgres struct {
	DatabaseURL string `mapstructure:"database_url" validate:"required"`
}
