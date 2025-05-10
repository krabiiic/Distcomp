package org.example.Impl.Kafka;

import org.example.Impl.DTO.PostRequestTo;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (key == null || ((PostRequestTo) key).getNewsId() == null) {
            return 0;
        }
        Long issueId = ((PostRequestTo) key).getNewsId();
        return Math.abs(issueId.hashCode()) % cluster.partitionCountForTopic(topic);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}