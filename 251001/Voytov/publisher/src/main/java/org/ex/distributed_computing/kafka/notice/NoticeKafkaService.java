package org.ex.distributed_computing.kafka.notice;

import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.NoticeRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeKafkaService {

  @Value("${spring.kafka.topics.notice.in}")
  private String noticeInTopicName;

  @Value("${spring.kafka.topics.notice.out}")
  private String noticeOutTopicName;

  private final KafkaTemplate<String, NoticeRequestDTO> kafkaTemplate;

  public void publish(NoticeRequestDTO payload, Long messageKey) {
    var message = MessageBuilder.withPayload(payload)
        .setHeader(KafkaHeaders.TOPIC, noticeInTopicName)
        .setHeader(KafkaHeaders.KEY, messageKey)
        .build();

    kafkaTemplate.send(message);
  }
}
