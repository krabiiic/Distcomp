// discussion/src/kafka.ts
import { Kafka } from 'kafkajs';
import { MessageController } from './controllers/message.controller.js';

const kafka = new Kafka({
    clientId: 'discussion-service',
    brokers: ['localhost:29092'], // <--- важно! внутри docker-сети
});

const consumer = kafka.consumer({ groupId: 'message-group' });
const messageController = new MessageController();

export async function startConsumer() {
    await consumer.connect();
    console.log('[Kafka] Start Consumer');
    await consumer.subscribe({ topic: 'message-topic', fromBeginning: true });

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            if (message.value) {
                try {
                    const message = JSON.parse(message.value.toString());
                    await messageController.createFromKafka(message);
                    console.log('[Kafka] Message processed:', message);
                } catch (err) {
                    console.error('[Kafka] Error processing message:', err);
                }
            }
        },
    });
}
