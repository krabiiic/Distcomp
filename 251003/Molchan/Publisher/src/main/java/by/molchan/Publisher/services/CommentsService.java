package by.molchan.Publisher.services;


import by.molchan.Publisher.DTOs.Requests.CommentRequestDTO;
import by.molchan.Publisher.DTOs.Responses.CommentResponseDTO;
import by.molchan.Publisher.DTOs.kafka.InTopicDTO;
import by.molchan.Publisher.DTOs.kafka.OutTopicDTO;
import by.molchan.Publisher.utils.exceptions.NotFoundException;
import by.molchan.Publisher.utils.mappers.CommentsMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.KafkaReplyTimeoutException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class CommentsService {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final CommentsMapper commentsMapper;
    private static final String IN_TOPIC = "InTopic";
    private static final String OUT_TOPIC = "OutTopic";

    @Autowired
    public CommentsService(ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate,
                        KafkaTemplate<String, InTopicDTO> kafkaTemplate,
                        CommentsMapper commentsMapper) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.commentsMapper = commentsMapper;
    }

    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        return commentsMapper.toCommentResponse(requestDTO);
    }

    public List<CommentResponseDTO> getAllComments() {
        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        return response.getCommentResponsesListDTO();
    }

    @Cacheable(value = "comments", key = "#id")
    public CommentResponseDTO getCommentById(Long id) {
        InTopicDTO request = new InTopicDTO(
                "GET",
                new CommentRequestDTO(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()){
            throw new NotFoundException(response.getError());
        }
        return response.getCommentResponseDTO();
    }
    @CacheEvict(value = "comments", key = "#commentRequestDTO.id")

    public CommentResponseDTO processCommentRequest(String httpMethod, CommentRequestDTO commentRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                commentRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, commentRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")){
            throw new NotFoundException(response.getError());
        }
        return response.getCommentResponseDTO();

    }


    private OutTopicDTO sendAndReceiveInternal(InTopicDTO request, String correlationId) {
        ProducerRecord<String, InTopicDTO> record = new ProducerRecord<>(IN_TOPIC, correlationId, request);
        RequestReplyFuture<String, InTopicDTO, OutTopicDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, OutTopicDTO> response = future.get();
            return response.value();
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaException("Error processing Kafka request", e);
        }
    }

}