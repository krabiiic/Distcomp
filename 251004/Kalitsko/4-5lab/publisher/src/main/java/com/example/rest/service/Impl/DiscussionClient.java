package com.example.rest.service.Impl;

import com.example.rest.dto.*;
import com.example.rest.mapper.PostMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Service
public class DiscussionClient {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostMapper postMapper;
    private static final String POST_CACHE_PREFIX = "post:";
    private static final String IN_TOPIC = "InTopic";
    private static final String OUT_TOPIC = "OutTopic";

    @Autowired
    public DiscussionClient(ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate,
                            KafkaTemplate<String, InTopicDTO> kafkaTemplate,
                            PostMapper postMapper, RedisTemplate<String, Object> redisTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.postMapper = postMapper;
        this.redisTemplate = redisTemplate;
    }

    public PostResponseTo createPost(PostRequestTo requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        String allPostsCacheKey = POST_CACHE_PREFIX + "all";
        redisTemplate.delete(allPostsCacheKey);

        return postMapper.toPostResponse(requestDTO);
    }

    public List<PostResponseTo> getAllPosts() {
        String cacheKey = POST_CACHE_PREFIX + "all";
        List<PostResponseTo> cachedPosts = (List<PostResponseTo>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedPosts != null && !cachedPosts.isEmpty()) {
            System.out.println("All posts found in Redis: " + cachedPosts);
            return cachedPosts;
        }

        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        redisTemplate.opsForValue().set(cacheKey, response.getPostResponseTos());
        return response.getPostResponseTos();
    }

    public PostResponseTo getPostById(Long id) {
        String cacheKey = POST_CACHE_PREFIX + id;

        PostResponseTo cachedPost = (PostResponseTo) redisTemplate.opsForValue().get(cacheKey);
        if (cachedPost != null) {
            System.out.println("Post found in Redis: " + cachedPost);
            return cachedPost;
        }

        InTopicDTO request = new InTopicDTO(
                "GET",
                new PostRequestTo(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()){
            throw new RuntimeException(response.getError());
        }
        redisTemplate.opsForValue().set(cacheKey, response.getPostResponseTo());

        return response.getPostResponseTo();
    }


    public PostResponseTo processPostRequest(String httpMethod, PostRequestTo commentRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                commentRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, commentRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")){
            throw new RuntimeException(response.getError());
        }

        String cacheKey = POST_CACHE_PREFIX + commentRequestDTO.getId();
        redisTemplate.delete(cacheKey);

        String allPostsCacheKey = POST_CACHE_PREFIX + "all";
        redisTemplate.delete(allPostsCacheKey);

        return response.getPostResponseTo();

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