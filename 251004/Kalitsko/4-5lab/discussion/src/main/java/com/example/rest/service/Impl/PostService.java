package com.example.rest.service.Impl;

import com.example.rest.dto.InTopicDTO;
import com.example.rest.dto.OutTopicDTO;
import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.entity.Post;
import com.example.rest.exceptionHandler.CreatorNotFoundException;
import com.example.rest.mapper.PostMapper;
import com.example.rest.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "posts-group")
    @SendTo
    public Message<OutTopicDTO> handlePostRequest(@Payload InTopicDTO request,
                                                     @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                     @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        PostRequestTo postRequestTo = request.getPostRequestTo();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(postRequestTo);
                return null;
            } else if (method.equals("GET")) {
                response = postRequestTo != null ? handleFindById(postRequestTo.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(postRequestTo);
            } else if (method.equals("DELETE")) {
                response = handleDelete(postRequestTo.getId());
            } else {
                response = new OutTopicDTO("Unsupported method: " + method, "DECLINE");
            }
        } catch (Exception ex) {
            response = new OutTopicDTO("Error: " + ex.getMessage(), "DECLINE");
        }

        if (replyTopic != null && correlationId != null) {
            return MessageBuilder.withPayload(response)
                    .setHeader(KafkaHeaders.TOPIC, new String(replyTopic))
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
        } else {
            return null;
        }
    }

    private OutTopicDTO handleSave(PostRequestTo dto) {
        Post post = postMapper.toEntity(dto);
        String country = "Default";
        post.setCountry(country);
        Post savedPost = postRepository.save(post);
        return new OutTopicDTO(postMapper.toResponse(savedPost), "APPROVE");
    }

    public List<PostResponseTo> findAll() {
        return postMapper.toPostResponseList(postRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<PostResponseTo> postResponseDTOList = findAll();
        return new OutTopicDTO(postResponseDTOList, "APPROVE");
    }

    public PostResponseTo findById(Long id) {
        List<Post> allPosts = postRepository.findAll();
        return allPosts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(postMapper::toResponse)
                .orElseThrow(() -> new CreatorNotFoundException(id));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }
    }

    private OutTopicDTO handleUpdate(PostRequestTo dto) {
        Post currPost = postRepository.findAll().stream()
                .filter(post -> post.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(dto.getId()));

        currPost.setContent(dto.getContent());
        Post updatedPost = postRepository.save(currPost);
        return new OutTopicDTO(postMapper.toResponse(updatedPost), "APPROVE");
    }

    public PostResponseTo update(PostRequestTo dto) {
        Post currPost = postRepository.findAll().stream()
                .filter(post -> post.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(dto.getId()));

        currPost.setContent(dto.getContent());
        Post updatedPost = postRepository.save(currPost);
        return postMapper.toResponse(updatedPost);
    }

    private OutTopicDTO handleDelete(Long id) {
        Post currPost = postRepository.findAll().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(id));
        if (currPost != null) {
            postRepository.delete(currPost);
        } else {
            throw new CreatorNotFoundException(id);
        }
        return new OutTopicDTO(postMapper.toResponse(currPost), "APPROVE");
    }

}
