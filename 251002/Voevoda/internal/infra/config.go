package infra

type RedisConfig struct {
	Address  string `mapstructure:"address" validate:"required"`
	Password string `mapstructure:"password" validate:"required"`
	DB       int    `mapstructure:"db" validate:"gte=0"`
}

type KafkaConfig struct {
	Brokers  []string `mapstructure:"brokers" validate:"required,min=1,dive"`
	InTopic  string   `mapstructure:"in_topic" validate:"required"`
	OutTopic string   `mapstructure:"out_topic" validate:"required"`
}
