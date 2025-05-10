package org.ex.distributed_computing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

  @Value("${spring.kafka.topics.notice.in}")
  private String noticeInTopicName;

  @Value("${spring.kafka.topics.notice.out}")
  private String noticeOutTopicName;

  @Bean
  public NewTopic inTopic() {
    return TopicBuilder.name(noticeInTopicName)
        .partitions(10)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic outTopic() {
    return TopicBuilder.name(noticeOutTopicName)
        .partitions(10)
        .replicas(1)
        .build();
  }
}
