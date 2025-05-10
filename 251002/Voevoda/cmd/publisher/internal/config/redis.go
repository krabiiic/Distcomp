package config

type Redis struct {
	Address  string `json:"address"`
	Password string `json:"password"`
	DB       int    `json:"db"`
}
