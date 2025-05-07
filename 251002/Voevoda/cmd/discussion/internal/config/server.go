package config

type Server struct {
	Address string `mapstructure:"address" validate:"required"`
}
