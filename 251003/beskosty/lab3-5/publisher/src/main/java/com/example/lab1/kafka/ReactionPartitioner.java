package com.example.lab1.kafka;

import com.example.lab1.dto.KafkaReactionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class ReactionPartitioner implements Partitioner {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        try {
            if (value instanceof KafkaReactionMessage) {
                KafkaReactionMessage message = (KafkaReactionMessage) value;
                Long issueId = message.getIssueId();
                int partitions = cluster.partitionCountForTopic(topic);
                if (partitions > 0 && issueId != null) {
                    return Math.abs(issueId.hashCode() % partitions);
                }
            } else if (value instanceof byte[]) {
                KafkaReactionMessage message = objectMapper.readValue((byte[]) value, KafkaReactionMessage.class);
                Long issueId = message.getIssueId();
                int partitions = cluster.partitionCountForTopic(topic);
                if (partitions > 0 && issueId != null) {
                    return Math.abs(issueId.hashCode() % partitions);
                }
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}