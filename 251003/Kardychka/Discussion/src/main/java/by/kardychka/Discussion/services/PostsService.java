package by.kardychka.Discussion.services;

import by.kardychka.Discussion.DTOs.Requests.PostRequestDTO;
import by.kardychka.Discussion.DTOs.Responses.PostResponseDTO;
import by.kardychka.Discussion.DTOs.kafka.InTopicDTO;
import by.kardychka.Discussion.DTOs.kafka.OutTopicDTO;
import by.kardychka.Discussion.models.Post;
import by.kardychka.Discussion.repositories.PostsRepository;
import by.kardychka.Discussion.utils.mappers.PostsMapper;
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
public class PostsService {

    private final PostsRepository postsRepository;
    private final PostsMapper postsMapper;

    @Value("${post.country}")
    private String country;

    @Autowired
    public PostsService(PostsRepository postsRepository, PostsMapper postsMapper) {
        this.postsRepository = postsRepository;
        this.postsMapper = postsMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "posts-group")
    @SendTo
    public Message<OutTopicDTO> handlePostRequest(@Payload InTopicDTO request,
                                                  @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                  @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        PostRequestDTO postRequestDTO = request.getPostRequestDTO();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(postRequestDTO);
                return null;
            } else if (method.equals("GET")) {
                response = postRequestDTO != null ? handleFindById(postRequestDTO.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(postRequestDTO);
            } else if (method.equals("DELETE")) {
                response = handleDelete(postRequestDTO.getId());
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


    private OutTopicDTO handleSave(PostRequestDTO dto) {
        Post post = postsMapper.toPost(dto);
        post.getKey().setId(dto.getId());
        post.getKey().setCountry(country);
        postsRepository.save(post);
        return new OutTopicDTO(postsMapper.toPostResponse(post), "APPROVE");
    }

    public List<PostResponseDTO> findAll() {
        return postsMapper.toPostResponseList(postsRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<PostResponseDTO> postResponseDTOList = findAll();
        return new OutTopicDTO(postResponseDTOList, "APPROVE");
    }

    public PostResponseDTO findById(Long id) {
        return postsMapper.toPostResponse(postsRepository.findByCountryAndId(country, id)
                .orElseThrow(() -> new RuntimeException("Post not found")));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }


    }

    private OutTopicDTO handleUpdate(PostRequestDTO dto) {
        Post post = postsMapper.toPost(dto);
        post.getKey().setId(dto.getId());
        post.getKey().setCountry(country);
        postsRepository.save(post);
        return new OutTopicDTO(postsMapper.toPostResponse(post), "APPROVE");
    }

    private OutTopicDTO handleDelete(Long id) {
        Optional<Post> optionalPost = postsRepository.findByCountryAndId(country, id);

        if (optionalPost.isEmpty()) {
            return new OutTopicDTO("Post not found", "DECLINE");
        }

        Post post = optionalPost.get();
        postsRepository.delete(post);
        return new OutTopicDTO(postsMapper.toPostResponse(post), "APPROVE");
    }

    public PostResponseDTO update(PostRequestDTO noteRequestDTO) {
        Post post = postsMapper.toPost(noteRequestDTO);
        post.getKey().setId(noteRequestDTO.getId());
        post.getKey().setCountry(country);
        return postsMapper.toPostResponse(postsRepository.save(post));
    }

}
