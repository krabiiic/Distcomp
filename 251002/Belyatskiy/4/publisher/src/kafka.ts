// article/src/kafka.ts
import { Kafka } from 'kafkajs';

const kafka = new Kafka({
    clientId: 'article-service',
    brokers: ['localhost:29092'], // Это PLAINTEXT_HOST из docker-compose
});

export const producer = kafka.producer();

export async function initKafka() {
    await producer.connect();
    console.log('[Kafka] Producer connected');
}
