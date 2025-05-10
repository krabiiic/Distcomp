package com.example.modulediscussion.kafka;

import com.example.modulepublisher.dto.MessageDTO;
import com.example.modulediscussion.entity.Message;
import com.example.modulediscussion.mapper.MessageMapper;
import com.example.modulediscussion.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private  final  KafkaProducerService kafkaProducerService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    public void listen(MessageDTO message) {
        log.info("Received Message: " + message);

        if ("CREATE".equals(message.getAction())) {
            Message tweet = messageMapper.toMessage(message);
            messageRepository.save(tweet);
            log.info("Message created and saved: " + tweet);

            sendModerationResult(message, "Created");

        } else if ("DELETE".equals(message.getAction())) {

            int id = message.getId();
            messageRepository.deleteById(id);
            log.info("Message deleted with ID: " + id);

            sendModerationResult(message, "Deleted");
        }
    }

    private String moderateMessage(MessageDTO message) {
        String[] stopWords = {"badword1", "badword2"};
        for (String stopWord : stopWords) {
            if (message.getContent().contains(stopWord)) {
                return "DECLINE";
            }
        }
        return "APPROVE";
    }

    private void sendModerationResult(MessageDTO message, String status) {
        MessageDTO responseMessage = new MessageDTO(message.getId(), message.getTweetId(),message.getContent(), status);
        kafkaProducerService.sendMessage("OutTopic", responseMessage);
    }
}
