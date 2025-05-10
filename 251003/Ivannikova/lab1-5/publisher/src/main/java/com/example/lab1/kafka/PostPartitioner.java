package com.example.lab1.kafka;

import com.example.lab1.dto.KafkaPostMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class PostPartitioner implements Partitioner {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        try {
            if (value instanceof KafkaPostMessage) {
                KafkaPostMessage message = (KafkaPostMessage) value;
                Long issueId = message.getIssueId();
                int partitions = cluster.partitionCountForTopic(topic);
                if (partitions > 0 && issueId != null) {
                    return Math.abs(issueId.hashCode() % partitions);
                }
            } else if (value instanceof byte[]) {
                KafkaPostMessage message = objectMapper.readValue((byte[]) value, KafkaPostMessage.class);
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