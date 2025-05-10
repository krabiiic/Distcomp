package org.ex.distributed_computing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

  @Value("${spring.kafka.topics.reaction.in}")
  private String reactionInTopicName;

  @Value("${spring.kafka.topics.reaction.out}")
  private String reactionOutTopicName;

  @Bean
  public NewTopic inTopic() {
    return TopicBuilder.name(reactionInTopicName)
        .partitions(10)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic outTopic() {
    return TopicBuilder.name(reactionOutTopicName)
        .partitions(10)
        .replicas(1)
        .build();
  }
}
