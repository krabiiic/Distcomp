package by.kardychka.Publisher.services;

import by.kardychka.Publisher.DTOs.Requests.PostRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.PostResponseDTO;
import by.kardychka.Publisher.DTOs.kafka.InTopicDTO;
import by.kardychka.Publisher.DTOs.kafka.OutTopicDTO;
import by.kardychka.Publisher.utils.exceptions.NotFoundException;
import by.kardychka.Publisher.utils.mappers.PostsMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.TimeoutException;


@Service
public class PostsService {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final PostsMapper postsMapper;
    private static final String IN_TOPIC = "InTopic";
    private static final String OUT_TOPIC = "OutTopic";

    @Autowired
    public PostsService(ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate,
                        KafkaTemplate<String, InTopicDTO> kafkaTemplate,
                        PostsMapper postsMapper) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.postsMapper = postsMapper;
    }

    public PostResponseDTO createPost(PostRequestDTO requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        return postsMapper.toPostResponse(requestDTO);
    }

    public List<PostResponseDTO> getAllPosts() {
        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        return response.getPostResponsesListDTO();
    }
    @Cacheable(value = "posts", key = "#id")
    public PostResponseDTO getPostById(Long id) {
        InTopicDTO request = new InTopicDTO(
                "GET",
                new PostRequestDTO(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()){
            throw new NotFoundException(response.getError());
        }
        return response.getPostResponseDTO();
    }

    @CacheEvict(value = "posts", key = "#postRequestDTO.id")
    public PostResponseDTO processPostRequest(String httpMethod, PostRequestDTO postRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                postRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, postRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")){
            throw new NotFoundException(response.getError());
        }
        return response.getPostResponseDTO();

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