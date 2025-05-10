from confluent_kafka import Producer
import logging

# Конфигурация Kafka
conf = {
    'bootstrap.servers': 'localhost:9092',  # Адрес Kafka брокера
    'client.id': 'python-producer'
}

# Инициализация продюсера
producer = Producer(conf)

def delivery_report(err, msg):
    """Функция обратного вызова для обработки успешной или неудачной отправки сообщения"""
    if err is not None:
        logging.error(f"Message delivery failed: {err}")
    else:
        logging.info(f"Message delivered to {msg.topic()} [{msg.partition()}]")

# Отправка сообщения в Kafka
def send_to_kafka(topic, message):
    producer.produce(topic, message.encode('utf-8'), callback=delivery_report)
    producer.flush()  # Ожидание завершения отправки
