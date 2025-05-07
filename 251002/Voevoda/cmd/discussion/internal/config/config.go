package config

import (
	"encoding/json"
	"log"
	"path/filepath"
	"strings"

	"github.com/go-playground/validator/v10"
	"github.com/joho/godotenv"
	"github.com/spf13/viper"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

type Config struct {
	Server    Server            `mapstructure:"server" validate:"required"`
	Cassandra Cassandra         `mapstructure:"cassandra" validate:"required"`
	Logger    Logger            `mapstructure:"logger" validate:"required"`
	Kafka     infra.KafkaConfig `mapstructure:"kafka" validate:"required"`
}

// Validate checks if the configuration is valid
func (c Config) Validate() error {
	validate := validator.New()

	return validate.Struct(&c)
}

func (c Config) String() string {
	c.Cassandra.Password = "[REDACTED]"
	c.Cassandra.Username = "[REDACTED]"
	c.Cassandra.Hosts = []string{"[REDACTED]"}
	c.Cassandra.Keyspace = "[REDACTED]"
	c.Cassandra.Consistency = "[REDACTED]"

	b, _ := json.MarshalIndent(c, "", "  ")

	return string(b)
}

func MustParseConfig(configPath string) Config {
	_ = godotenv.Load()
	v := viper.New()

	configDir := setConfigName(v, configPath)

	v.SetConfigType("yaml")
	v.AddConfigPath(configDir)

	setDefaults(v)

	if err := v.ReadInConfig(); err != nil {
		log.Printf("Warning reading config file: %s, will use defaults and environment variables\n", err)
	}

	v.SetEnvPrefix("PUBLISHER")
	v.SetEnvKeyReplacer(strings.NewReplacer(".", "_"))
	v.AutomaticEnv()

	bindEnvVars(v)

	var cfg Config
	if err := v.Unmarshal(&cfg); err != nil {
		log.Fatalf("Unable to decode config into struct: %v", err)
	}

	if err := cfg.Validate(); err != nil {
		log.Fatalf("Invalid configuration: %v", err)
	}

	log.Printf("Loaded configuration: %s\n", cfg.String())

	return cfg
}

func setConfigName(v *viper.Viper, configPath string) string {
	configFileInfo, err := filepath.Abs(configPath)
	if err == nil && filepath.Ext(configFileInfo) != "" {
		configFileName := strings.TrimSuffix(filepath.Base(configFileInfo), filepath.Ext(configFileInfo))
		v.SetConfigName(configFileName)

		return filepath.Dir(configFileInfo)
	}

	v.SetConfigName("config")

	return configPath
}

func setDefaults(v *viper.Viper) {
	v.SetDefault("server.address", ":8080")
	v.SetDefault("logger.level", "info")
	v.SetDefault("logger.format", "json")
	v.SetDefault("logger.add_source", false)
	v.SetDefault("kafka.in_topic", "input")
	v.SetDefault("kafka.out_topic", "output")
}

func bindEnvVars(v *viper.Viper) {
	envMappings := map[string]string{
		"server.address":        "SERVER_ADDRESS",
		"cassandra.hosts":       "CASSANDRA_HOSTS",
		"cassandra.keyspace":    "CASSANDRA_KEYSPACE",
		"cassandra.username":    "CASSANDRA_USERNAME",
		"cassandra.password":    "CASSANDRA_PASSWORD",
		"cassandra.consistency": "CASSANDRA_CONSISTENCY",
		"logger.level":          "LOGGER_LEVEL",
		"logger.format":         "LOGGER_FORMAT",
		"logger.add_source":     "LOGGER_ADD_SOURCE",
		"kafka.brokers":         "KAFKA_BROKERS",
		"kafka.in_topic":        "KAFKA_IN_TOPIC",
		"kafka.out_topic":       "KAFKA_OUT_TOPIC",
	}

	for configKey, envVar := range envMappings {
		bindEnv(v, configKey, envVar)
	}
}

func bindEnv(v *viper.Viper, configKey, envVar string) {
	if err := v.BindEnv(configKey, envVar); err != nil {
		log.Printf("Error binding env var %s: %s\n", envVar, err)
	}
}
