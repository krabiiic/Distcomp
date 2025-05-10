export const KAFKA_BROKER = 'localhost:9092'; // As defined in docker-compose KAFKA_ADVERTISED_LISTENERS
export const KAFKA_CLIENT_ID_PUBLISHER = 'publisher-client';
export const KAFKA_CLIENT_ID_DISCUSSION = 'discussion-client';
export const KAFKA_GROUP_ID_DISCUSSION = 'discussion-group'; // Consumer group for discussion
export const KAFKA_GROUP_ID_PUBLISHER = 'publisher-group';   // Consumer group for publisher (to get responses)

// Topic for requests (Publisher -> Discussion)
export const TOPIC_REACTION_REQUEST = 'reaction-requests'; // InTopic
// Topic for responses (Discussion -> Publisher)
export const TOPIC_REACTION_RESPONSE = 'reaction-responses'; // OutTopic

export enum ReactionState {
    PENDING = 'PENDING',
    APPROVED = 'APPROVED', // Changed from APPROVE for consistency
    DECLINED = 'DECLINED', // Changed from DELCINE
}

export enum KafkaAction {
    CREATE = 'CREATE',
    UPDATE = 'UPDATE',
    DELETE = 'DELETE',
}

// Interface for Kafka messages
export interface ReactionKafkaMessage {
    action: KafkaAction;
    payload: any; // Can be ReactionRequestTo for CREATE/UPDATE, or just { id: number } for DELETE
    messageId?: string; // Optional: for tracing
}

export interface ReactionResponseMessage {
    status: 'SUCCESS' | 'FAILURE';
    state?: ReactionState; // Include the final state
    originalAction: KafkaAction;
    payload: any; // The processed reaction or error info
    messageId?: string; // Correlate with request
}