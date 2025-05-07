package config

import (
	"time"

	"github.com/spf13/viper"
)

type Config struct {
	Log      LogConfig
	Core     CoreConfig
	GRPC     GRPCServerConfig
	HTTP     HTTPServerConfig
	Kafka    KafkaConfig
	Casandra CasandraConfig
}

type CoreConfig struct {
	ShutdownTimeout time.Duration
}

type CasandraConfig struct {
	Addrs    []string
	Keyspace string
	User     string
	Password string
}

type LogConfig struct {
	Level    string
	Encoding string
}

type GRPCServerConfig struct {
	Host    string
	Port    string
	Timeout time.Duration
}

type HTTPServerConfig struct {
	Host        string
	Port        string
	Timeout     time.Duration
	IdleTimeout time.Duration
}

type KafkaConfig struct {
	Brokers  []string
	GroupID  string
	Topic    string
	User     string
	Password string
}

func Load() Config {
	return Config{
		Log: LogConfig{
			Level:    viper.GetString("log.level"),
			Encoding: viper.GetString("log.encoding"),
		},
		Core: CoreConfig{
			ShutdownTimeout: viper.GetDuration("core.shutdown_timeout"),
		},
		Casandra: CasandraConfig{
			Addrs:    viper.GetStringSlice("cassandra.addrs"),
			Keyspace: viper.GetString("cassandra.keyspace"),
			User:     viper.GetString("cassandra.user"),
			Password: viper.GetString("cassandra.password"),
		},
		GRPC: GRPCServerConfig{
			Host:    viper.GetString("grpc.host"),
			Port:    viper.GetString("grpc.port"),
			Timeout: viper.GetDuration("grpc.timeout"),
		},
		HTTP: HTTPServerConfig{
			Host:        viper.GetString("http.host"),
			Port:        viper.GetString("http.port"),
			Timeout:     viper.GetDuration("http.timeout"),
			IdleTimeout: viper.GetDuration("http.idle_timeout"),
		},
		Kafka: KafkaConfig{
			Brokers:  viper.GetStringSlice("kafka.brokers"),
			GroupID:  viper.GetString("kafka.group_id"),
			Topic:    viper.GetString("kafka.topic"),
			User:     viper.GetString("kafka.user"),
			Password: viper.GetString("kafka.password"),
		},
	}
}
