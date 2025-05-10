variable "POSTGRES_HOST" {
  type    = string
  default = "postgres"
}

variable "POSTGRES_DATA" {
  type    = string
  default = "/var/lib/pgdata"
}

variable "POSTGRES_PORT" {
  type    = number
  default = 5432
}

variable "POSTGRES_USER" {
  type    = string
  default = "postgres"
}

variable "POSTGRES_PASSWORD" {
  type    = string
  default = "postgres"
}

variable "POSTGRES_DB" {
  type    = string
  default = "distcomp"
}

variable "CASSANDRA_USER" {
  type    = string
  default = "admin"
}

variable "CASSANDRA_PASSWORD" {
  type    = string
  default = "admin"
}

variable "CASSANDRA_KEYSPACE" {
  type    = string
  default = "distcomp"
}

variable "CASSANDRA_HOST" {
  type    = string
  default = "cassandra"
}

variable "CASSANDRA_PORT" {
  type    = number
  default = 9042
}

variable "CASSANDRA_CLUSTER_NAME" {
  type    = string
  default = "cassandra"
}

variable "CASSANDRA_PASSWORD_SEEDER" {
  type    = string
  default = "yes"
}

variable "CASSANDRA_ADDRS" {
  type    = string
  default = "cassandra:9042"
}

variable "KAFKA_NODE_ID" {
  type    = number
  default = 0
}

variable "KAFKA_CLIENT_USERS" {
  type    = string
  default = "admin"
}

variable "KAFKA_CLIENT_PASSWORDS" {
  type    = string
  default = "admin"
}

variable "KAFKA_HOST" {
  type    = string
  default = "kafka"
}

variable "KAFKA_BROKERS" {
  type    = string
  default = "kafka:9093"
}

variable "KAFKA_TOPIC_IN" {
  type    = string
  default = "notice_in"
}

variable "KAFKA_TOPIC_OUT" {
  type    = string
  default = "notice_out"
}

variable "API_SERVICE_HOST" {
  type    = string
  default = "api"
}

variable "API_SERVICE_PORT" {
  type    = number
  default = 24110
}

variable "NOTICE_SERVICE_HOST" {
  type    = string
  default = "notice"
}

variable "NOTICE_SERVICE_PORT" {
  type    = number
  default = 24130
}
