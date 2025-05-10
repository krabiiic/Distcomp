terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.2"
    }
  }
}

provider "docker" {
  host = "unix:///var/run/docker.sock"
}

resource "docker_image" "distcomp" {
  name = "khmelov/distcomp"
}

resource "docker_container" "distcomp" {
  name     = "distcomp"
  image    = docker_image.distcomp.image_id
  hostname = "distcomp"
  restart  = "unless-stopped"

  memory = 1024

  networks_advanced {
    name = docker_network.dist_net.id
  }

  ports {
    internal = 24100
    external = 24100
  }
}

resource "docker_image" "api" {
  name = "distcomp/api"
}

resource "docker_container" "api" {
  name     = "distcomp_api"
  image    = docker_image.api.image_id
  hostname = var.API_SERVICE_HOST
  restart  = "unless-stopped"

  memory = 512

  env = [
    "API_STORAGE_HOST=${var.POSTGRES_HOST}",
    "API_STORAGE_PORT=${var.POSTGRES_PORT}",
    "API_STORAGE_USER=${var.POSTGRES_USER}",
    "API_STORAGE_PASSWORD=${var.POSTGRES_PASSWORD}",
    "API_STORAGE_DBNAME=${var.POSTGRES_DB}",
    "API_STORAGE_BROKERS=${var.KAFKA_BROKERS}",
    "API_KAFKA_BROKERS=${var.KAFKA_BROKERS}",
    "API_KAFKA_TOPIC=${var.KAFKA_TOPIC_IN}",
    "API_NOTICE_SERVICE_ADDR=${var.NOTICE_SERVICE_HOST}:${var.NOTICE_SERVICE_PORT}",
  ]

  networks_advanced {
    name = docker_network.api_net.id
  }

  networks_advanced {
    name = docker_network.db_net.id
  }

  networks_advanced {
    name = docker_network.kafka_net.id
  }

  ports {
    internal = var.API_SERVICE_PORT
    external = 24110
  }
}

resource "docker_image" "notice_api" {
  name = "distcomp/notice"
}

resource "docker_container" "notice_api" {
  name     = "distcomp_notice_api"
  image    = docker_image.notice_api.image_id
  hostname = var.NOTICE_SERVICE_HOST
  restart  = "unless-stopped"

  memory = 512

  env = [
    "NOTICE_API_CASSANDRA_ADDRS=${var.CASSANDRA_ADDRS}",
    "NOTCIE_API_CASSANDRA_USER=${var.CASSANDRA_USER}",
    "NOTCIE_API_CASSANDRA_KEYSPACE=${var.CASSANDRA_KEYSPACE}",
    "NOTCIE_API_CASSANDRA_PASSWORD=${var.CASSANDRA_PASSWORD}",
    "NOTCIE_API_KAFKA_BROKERS=${var.KAFKA_BROKERS}",
    "NOTCIE_API_KAFKA_TOPIC=${var.KAFKA_TOPIC_IN}",
  ]

  networks_advanced {
    name    = docker_network.api_net.id
    aliases = ["${var.NOTICE_SERVICE_HOST}"]
  }

  networks_advanced {
    name = docker_network.db_net.id
  }

  networks_advanced {
    name = docker_network.kafka_net.id
  }

  ports {
    internal = var.NOTICE_SERVICE_PORT
    external = 24130
  }
}

resource "docker_image" "postgres" {
  name = "postgres:16.0-alpine"
}

resource "docker_container" "postgres" {
  name     = "distcomp_postgres"
  image    = docker_image.postgres.image_id
  hostname = var.POSTGRES_HOST
  restart  = "unless-stopped"

  memory = 1024

  env = [
    "PGDATA=${var.POSTGRES_DATA}",
    "PGPORT=${var.POSTGRES_PORT}",
    "POSTGRES_USER=${var.POSTGRES_USER}",
    "POSTGRES_PASSWORD=${var.POSTGRES_PASSWORD}",
    "POSTGRES_DB=${var.POSTGRES_DB}"
  ]

  networks_advanced {
    name    = docker_network.db_net.id
    aliases = ["${var.POSTGRES_HOST}"]
  }

  healthcheck {
    test         = ["CMD-SHELL", "pg_isready", "-d", "${var.POSTGRES_DB}"]
    interval     = "10s"
    timeout      = "5s"
    retries      = 3
    start_period = "10s"
  }

  ports {
    internal = var.POSTGRES_PORT
    external = 5432
  }
}

resource "docker_image" "migrator" {
  name = "gomicro/goose:3.24.1"
}

resource "docker_container" "migrator" {
  name     = "distcomp_migrator"
  image    = docker_image.migrator.image_id
  hostname = "migrator"
  restart  = "on-failure"

  env = [
    "GOOSE_DRIVER=postgres",
    "GOOSE_MIGRATION_DIR=/migrations",
    "GOOSE_DBSTRING=postgres://${var.POSTGRES_USER}:${var.POSTGRES_PASSWORD}@${var.POSTGRES_HOST}:${var.POSTGRES_PORT}/${var.POSTGRES_DB}"
  ]

  networks_advanced {
    name = docker_network.db_net.id
  }

  volumes {
    container_path = "/migrations"
    host_path      = "${path.cwd}/migrations/postgres"
    read_only      = true
  }

  command = ["goose", "up"]
}

resource "docker_image" "cassandra" {
  name = "bitnami/cassandra:5.0.3-debian-12-r5"
}

resource "docker_container" "cassandra" {
  name     = "distcomp_cassandra"
  image    = docker_image.cassandra.image_id
  hostname = var.CASSANDRA_HOST
  restart  = "unless-stopped"

  memory = 4096

  env = [
    "CASSANDRA_USER=${var.CASSANDRA_USER}",
    "CASSANDRA_HOST=${var.CASSANDRA_HOST}",
    "CASSANDRA_PASSWORD=${var.CASSANDRA_PASSWORD}",
    "CASSANDRA_CLUSTER_NAME=${var.CASSANDRA_CLUSTER_NAME}",
    "CASSANDRA_PASSWORD_SEEDER=${var.CASSANDRA_PASSWORD_SEEDER}"
  ]

  networks_advanced {
    name    = docker_network.db_net.id
    aliases = [var.CASSANDRA_HOST]
  }

  ports {
    internal = 9042
    external = 9042
  }

  volumes {
    host_path      = "${path.cwd}/migrations/cassandra"
    container_path = "/docker-entrypoint-initdb.d"
    read_only      = true
  }

  healthcheck {
    test         = ["CMD-SHELL", "nodetool", "state"]
    interval     = "30s"
    timeout      = "20s"
    retries      = 5
    start_period = "60s"
  }
}

resource "docker_image" "kafka" {
  name = "bitnami/kafka:latest"
}

resource "docker_container" "kafka" {
  name     = "distcomp_kafka"
  image    = docker_image.kafka.image_id
  hostname = var.KAFKA_HOST
  restart  = "unless-stopped"

  memory = 1024

  env = [
    "KAFKA_CFG_NODE_ID=${var.KAFKA_NODE_ID}",
    "KAFKA_CLIENT_USERS=${var.KAFKA_CLIENT_USERS}",
    "KAFKA_CLIENT_PASSWORDS=${var.KAFKA_CLIENT_PASSWORDS}",
    "KAFKA_CFG_PROCESS_ROLES=controller,broker",
    "KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9094",
    "KAFKA_CFG_LISTENERS=INT://kafka:9092,PLAINTEXT://kafka:9093,CONTROLLER://kafka:9094",
    "KAFKA_CFG_ADVERTISED_LISTENERS=INT://localhost:9092,PLAINTEXT://kafka:9093",
    "KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,INT:PLAINTEXT",
    "KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER",
    "KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true",
    "ALLOW_PLAINTEXT_LISTENER=yes",
  ]

  networks_advanced {
    name    = docker_network.kafka_net.id
    aliases = [var.KAFKA_HOST]
  }

  ports {
    internal = 9092
    external = 9092
  }
}

resource "docker_network" "dist_net" {
  name   = "distcomp_dist_net"
  driver = "bridge"
}

resource "docker_network" "api_net" {
  name   = "distcomp_api_net"
  driver = "bridge"
}

resource "docker_network" "db_net" {
  name   = "distcomp_db_net"
  driver = "bridge"
}

resource "docker_network" "kafka_net" {
  name   = "distcomp_kafka_net"
  driver = "bridge"
}
