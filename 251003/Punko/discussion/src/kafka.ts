// discussion/src/kafka.ts
import { Kafka } from 'kafkajs';
import { ReactionController } from './controllers/reaction.controller.js';

const kafka = new Kafka({
    clientId: 'discussion-service',
    brokers: ['localhost:29092'], // <--- важно! внутри docker-сети
});

const consumer = kafka.consumer({ groupId: 'reaction-group' });
const reactionController = new ReactionController();

export async function startConsumer() {
    await consumer.connect();
    console.log('[Kafka] Start Consumer');
    await consumer.subscribe({ topic: 'reaction-topic', fromBeginning: true });

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            if (message.value) {
                try {
                    const reaction = JSON.parse(message.value.toString());
                    await reactionController.createFromKafka(reaction);
                    console.log('[Kafka] Reaction processed:', reaction);
                } catch (err) {
                    console.error('[Kafka] Error processing reaction:', err);
                }
            }
        },
    });
}
