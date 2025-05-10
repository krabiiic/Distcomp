package by.molchan.Discussion.services;


import by.molchan.Discussion.DTOs.Requests.CommentRequestDTO;
import by.molchan.Discussion.DTOs.Responses.CommentResponseDTO;
import by.molchan.Discussion.DTOs.kafka.InTopicDTO;
import by.molchan.Discussion.DTOs.kafka.OutTopicDTO;
import by.molchan.Discussion.models.Comment;
import by.molchan.Discussion.repositories.CommentsRepository;
import by.molchan.Discussion.utils.mappers.CommentsMapper;
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
import java.util.UUID;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;
    @Value("${comment.country}")
    private String country;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository, CommentsMapper commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "comments-group")
    @SendTo
    public Message<OutTopicDTO> handleCommentRequest(@Payload InTopicDTO request,
                                                     @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                     @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        CommentRequestDTO commentRequestDTO = request.getCommentRequestDTO();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(commentRequestDTO);
                return null;
            } else if (method.equals("GET")) {
                response = commentRequestDTO != null ? handleFindById(commentRequestDTO.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(commentRequestDTO);
            } else if (method.equals("DELETE")) {
                response = handleDelete(commentRequestDTO.getId());
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


    private OutTopicDTO handleSave(CommentRequestDTO dto) {
        Comment comment = commentsMapper.toComment(dto);
        comment.getKey().setId(dto.getId());
        comment.getKey().setCountry(country);
        commentsRepository.save(comment);
        return new OutTopicDTO(commentsMapper.toCommentResponse(comment), "APPROVE");
    }

    public List<CommentResponseDTO> findAll() {
        return commentsMapper.toCommentResponseList(commentsRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<CommentResponseDTO> commentResponseDTOList = findAll();
        return new OutTopicDTO(commentResponseDTOList, "APPROVE");
    }

    public CommentResponseDTO findById(Long id) {
        return commentsMapper.toCommentResponse(commentsRepository.findByCountryAndId(country, id)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }


    }

    private OutTopicDTO handleUpdate(CommentRequestDTO dto) {
        Comment comment = commentsMapper.toComment(dto);
        comment.getKey().setId(dto.getId());
        comment.getKey().setCountry(country);
        commentsRepository.save(comment);
        return new OutTopicDTO(commentsMapper.toCommentResponse(comment), "APPROVE");
    }

    private OutTopicDTO handleDelete(Long id) {
        Optional<Comment> optionalComment = commentsRepository.findByCountryAndId(country, id);

        if (optionalComment.isEmpty()) {
            return new OutTopicDTO("Comment not found", "DECLINE");
        }

        Comment comment = optionalComment.get();
        commentsRepository.delete(comment);
        return new OutTopicDTO(commentsMapper.toCommentResponse(comment), "APPROVE");
    }
    public CommentResponseDTO update(CommentRequestDTO commentRequestDTO) {
        Comment comment = commentsMapper.toComment(commentRequestDTO);
        comment.getKey().setId(commentRequestDTO.getId());
        comment.getKey().setCountry(country);
        return commentsMapper.toCommentResponse(commentsRepository.save(comment));
    }
}