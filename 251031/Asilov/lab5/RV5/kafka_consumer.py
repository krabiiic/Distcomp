from confluent_kafka import Consumer, KafkaException, KafkaError

# Конфигурация потребителя
consumer_conf = {
    'bootstrap.servers': 'localhost:9092',
    'group.id': 'python-consumer',
    'auto.offset.reset': 'earliest'  # Для чтения с самого начала
}

consumer = Consumer(consumer_conf)

def consume_messages():
    consumer.subscribe(['task_created'])  # Подписываемся на топик "task_created"
    try:
        while True:
            msg = consumer.poll(1.0)  # Ждем сообщения
            if msg is None:
                continue  # Если сообщений нет, продолжаем ждать
            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    print(f"End of partition reached {msg.partition}")
                else:
                    raise KafkaException(msg.error())
            else:
                # Обработка полученного сообщения
                print(f"Received message: {msg.value().decode('utf-8')}")
    finally:
        consumer.close()
