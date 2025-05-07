package by.zdanovich.Publisher.services;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Publisher.DTOs.kafka.InTopicDTO;
import by.zdanovich.Publisher.DTOs.kafka.OutTopicDTO;
import by.zdanovich.Publisher.utils.exceptions.NotFoundException;
import by.zdanovich.Publisher.utils.mappers.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.KafkaReplyTimeoutException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final MessageMapper messageMapper;
    private static final String IN_TOPIC = "InTopic";

    public MessageResponseDTO createMessage(MessageRequestDTO requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        return messageMapper.toMarkResponse(requestDTO);
    }

    public List<MessageResponseDTO> getAllMessages() {
        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        return response.getMessageResponseDTOList();
    }

    @Cacheable(value = "messages", key = "#id")
    public MessageResponseDTO getMessageById(Long id) {
        InTopicDTO request = new InTopicDTO(
                "GET",
                new MessageRequestDTO(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()) {
            throw new NotFoundException(response.getError());
        }
        return response.getMessageResponseDTO();
    }

    @CacheEvict(value = "messages", key = "#messageRequestDTO.id")
    public MessageResponseDTO processMessageRequest(String httpMethod, MessageRequestDTO messageRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                messageRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, messageRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")) {
            throw new NotFoundException(response.getError());
        }
        return response.getMessageResponseDTO();

    }


    private OutTopicDTO sendAndReceiveInternal(InTopicDTO request, String correlationId) {
        ProducerRecord<String, InTopicDTO> record = new ProducerRecord<>(IN_TOPIC, correlationId, request);
        RequestReplyFuture<String, InTopicDTO, OutTopicDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, OutTopicDTO> response = future.get(10, TimeUnit.SECONDS);
            return response.value();
        } catch (TimeoutException e) {
            throw new KafkaReplyTimeoutException("Timeout waiting for message data");
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaException("Error processing Kafka request", e);
        }
    }

}