package config

type Cassandra struct {
	Hosts       []string `mapstructure:"hosts" validate:"required,min=1,dive"`
	Keyspace    string   `mapstructure:"keyspace" validate:"required"`
	Username    string   `mapstructure:"username"`
	Password    string   `mapstructure:"password"`
	Consistency string   `mapstructure:"consistency" validate:"required,oneof=any one quorum all local serial"`
}
