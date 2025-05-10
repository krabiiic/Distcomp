.PHONY: run-publisher
run-publisher:
	go run cmd/publisher/main.go -config configs/publisher.yaml

.PHONY: run-discussion
run-discussion:
	go run cmd/discussion/main.go -config configs/discussion.yaml

.PHONY: fmt
fmt:
	golines -w .
	gofumpt -w .

.PHONY: lint
lint:
	golangci-lint run

.PHONY: run-psql
run-psql:
	docker run --name psql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=distcomp -d -p 5432:5432 postgres

.PHONY: run-cassandra
run-cassandra:
	docker run --name cassandra-instance -p 9042:9042 -d cassandra:latest
	@echo "Waiting for Cassandra to start..."
	@sleep 60
	@echo "Cassandra should be ready now."
	docker exec -it cassandra-instance cqlsh -e "CREATE KEYSPACE IF NOT EXISTS distcomp WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };"

.PHONY: run-kafka
run-kafka:
	docker network create kafkanet || true
	docker run -d --network=kafkanet --name=zookeeper \
		-e ZOOKEEPER_CLIENT_PORT=2181 \
		-e ZOOKEEPER_TICK_TIME=2000 \
		-p 2181:2181 \
		confluentinc/cp-zookeeper
	docker run -d --network=kafkanet --name=kafka \
		-e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
		-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
		-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
		-p 9092:9092 \
		confluentinc/cp-kafka

.PHONY: run-redis
run-redis:
	docker run --name redis-instance \
		-p 6379:6379 \
		-e REDIS_PASSWORD=onoshko_enjoyer \
		-d redis:latest redis-server --requirepass onoshko_enjoyer
