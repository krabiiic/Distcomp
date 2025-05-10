package org.ex.distributed_computing.kafka.reaction;

import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.ReactionRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionKafkaService {

  @Value("${spring.kafka.topics.reaction.in}")
  private String reactionInTopicName;

  @Value("${spring.kafka.topics.reaction.out}")
  private String reactionOutTopicName;

  private final KafkaTemplate<String, ReactionRequestDTO> kafkaTemplate;

  public void publish(ReactionRequestDTO payload, Long messageKey) {
    var message = MessageBuilder.withPayload(payload)
        .setHeader(KafkaHeaders.TOPIC, reactionInTopicName)
        .setHeader(KafkaHeaders.KEY, messageKey)
        .build();

    kafkaTemplate.send(message);
  }
}
