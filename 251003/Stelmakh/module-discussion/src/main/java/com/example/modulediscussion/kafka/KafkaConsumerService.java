package com.example.modulediscussion.kafka;

import com.example.modulepublisher.dto.ReactionDTO;
import com.example.modulediscussion.entity.Reaction;
import com.example.modulediscussion.mapper.ReactionMapper;
import com.example.modulediscussion.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private  final  KafkaProducerService kafkaProducerService;
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    public void listen(ReactionDTO message) {
        log.info("Received Message: " + message);

        if ("CREATE".equals(message.getAction())) {
            Reaction tweet = reactionMapper.toReaction(message);
            reactionRepository.save(tweet);
            log.info("Reaction created and saved: " + tweet);

            sendModerationResult(message, "Created");

        } else if ("DELETE".equals(message.getAction())) {

            int id = message.getId();
            reactionRepository.deleteById(id);
            log.info("Reaction deleted with ID: " + id);

            sendModerationResult(message, "Deleted");
        }
    }

    private String moderateReaction(ReactionDTO message) {
        String[] stopWords = {"badword1", "badword2"};
        for (String stopWord : stopWords) {
            if (message.getContent().contains(stopWord)) {
                return "DECLINE";
            }
        }
        return "APPROVE";
    }

    private void sendModerationResult(ReactionDTO message, String status) {
        ReactionDTO responseMessage = new ReactionDTO(message.getId(), message.getIssueId(),message.getContent(), status);
        kafkaProducerService.sendReaction("OutTopic", responseMessage);
    }
}
