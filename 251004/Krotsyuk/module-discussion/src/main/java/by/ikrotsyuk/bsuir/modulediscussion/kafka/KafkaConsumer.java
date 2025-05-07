package by.ikrotsyuk.bsuir.modulediscussion.kafka;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.*;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaExceptionDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionListResponseDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionResponseDTO;
import by.ikrotsyuk.bsuir.modulediscussion.entity.ReactionEntity;
import by.ikrotsyuk.bsuir.modulediscussion.mapper.ReactionMapper;
import by.ikrotsyuk.bsuir.modulediscussion.repository.ReactionRepository;
import by.ikrotsyuk.bsuir.modulediscussion.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class KafkaConsumer {
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final KafkaProducer kafkaProducer;
    private final IdGenerator idGenerator;

    @Transactional
    @KafkaListener(topics = KafkaConstants.IN_TOPIC_NAME)
    public void handle(ConsumerRecord<String, Object> record){
        String eventId = record.key();
        Object event = record.value();

        Object response;
        try{
            response = processResponse(event, eventId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        kafkaProducer.sendResponse(response, eventId);
    }

    private Object processResponse(Object event, String eventId){
        if(event.getClass() == KafkaGetReactionEvent.class){
            KafkaGetReactionEvent request = (KafkaGetReactionEvent) event;
            Optional<ReactionEntity> optionalReactionEntity = reactionRepository.findById(request.getReactionId());
            if(optionalReactionEntity.isPresent())
                return new KafkaReactionResponseDTO(eventId, reactionMapper.toReactionResponse(optionalReactionEntity.get()));
            else
                return new KafkaExceptionDTO("not found");
        } else if (event.getClass() == KafkaGetReactionsEvent.class) {
            List<ReactionEntity> reactionEntityList = reactionRepository.findAll();
            return new KafkaReactionListResponseDTO(eventId, reactionMapper.toReactionResponseList(reactionEntityList));
        } else if (event.getClass() == KafkaCreateReactionEvent.class) {
            KafkaCreateReactionEvent request = (KafkaCreateReactionEvent) event;
            ReactionEntity reactionEntity = reactionMapper.toKafkaEntity(request.getData());
            reactionEntity.setId(idGenerator.generateSequence());
            reactionEntity = reactionRepository.save(reactionEntity);
            return new KafkaReactionResponseDTO(eventId, reactionMapper.toReactionResponse(reactionEntity));
        } else if (event.getClass() == KafkaUpdateReactionEvent.class) {
            KafkaUpdateReactionEvent request = (KafkaUpdateReactionEvent) event;
            Optional<ReactionEntity> optionalReactionEntity = reactionRepository.findById(request.getReactionId());
            if(optionalReactionEntity.isPresent()){
                ReactionEntity reactionEntity = optionalReactionEntity.get();
                reactionEntity.setArticleId(request.getData().getArticleId());
                reactionEntity.setCountry(request.getData().getCountry());
                reactionEntity.setContent(request.getData().getContent());
                return new KafkaReactionResponseDTO(eventId, reactionMapper.toReactionResponse(reactionRepository.save(reactionEntity)));
            }else{
                return new KafkaExceptionDTO("not found");
            }
        } else if (event.getClass() == KafkaDeleteReactionEvent.class) {
            KafkaDeleteReactionEvent request = (KafkaDeleteReactionEvent) event;
            Optional<ReactionEntity> optionalReactionEntity = reactionRepository.findById(request.getReactionId());
            if(optionalReactionEntity.isPresent()){
                ReactionEntity reactionEntity = optionalReactionEntity.get();
                reactionRepository.deleteById(request.getReactionId());
                return new KafkaReactionResponseDTO(eventId, reactionMapper.toReactionResponse(reactionEntity));
            }else
                return new KafkaExceptionDTO("not found");
        }else
            return new KafkaExceptionDTO("error");
    }
}
