list-topics:
	docker exec $(word 2, $(MAKECMDGOALS)) /bin/sh -c "kafka-topics --bootstrap-server localhost:9092 --list"

list-msgs:
	docker exec $(word 2, $(MAKECMDGOALS)) /bin/sh -c "kafka-console-consumer --bootstrap-server localhost:9092 --topic $(word 3, $(MAKECMDGOALS)) --from-beginning"
	
list-consumer-groups:
	docker exec $(word 2, $(MAKECMDGOALS)) /bin/sh -c "kafka-consumer-groups --bootstrap-server localhost:9092 --list" 
