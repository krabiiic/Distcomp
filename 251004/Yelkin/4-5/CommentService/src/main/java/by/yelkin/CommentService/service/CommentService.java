package by.yelkin.CommentService.service;

import by.yelkin.CommentService.dto.InTopicDTO;
import by.yelkin.CommentService.dto.OutTopicDTO;
import by.yelkin.CommentService.dto.CommentRequestTo;
import by.yelkin.CommentService.dto.CommentResponseTo;
import by.yelkin.CommentService.entity.Comment;
import by.yelkin.CommentService.exceptionHandler.CreatorNotFoundException;
import by.yelkin.CommentService.mapper.CommentMapper;
import by.yelkin.CommentService.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "posts-group")
    @SendTo
    public Message<OutTopicDTO> handleCommentRequest(@Payload InTopicDTO request,
                                                     @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                     @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        CommentRequestTo commentRequestTo = request.getCommentRequestTo();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(commentRequestTo);
                return null;
            } else if (method.equals("GET")) {
                response = commentRequestTo != null ? handleFindById(commentRequestTo.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(commentRequestTo);
            } else if (method.equals("DELETE")) {
                response = handleDelete(commentRequestTo.getId());
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

    private OutTopicDTO handleSave(CommentRequestTo dto) {
        Comment comment = commentMapper.toEntity(dto);
        String country = "Default";
        comment.setCountry(country);
        Comment savedComment = commentRepository.save(comment);
        return new OutTopicDTO(commentMapper.toResponse(savedComment), "APPROVE");
    }

    public List<CommentResponseTo> findAll() {
        return commentMapper.toPostResponseList(commentRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<CommentResponseTo> postResponseDTOList = findAll();
        return new OutTopicDTO(postResponseDTOList, "APPROVE");
    }

    public CommentResponseTo findById(Long id) {
        List<Comment> allComments = commentRepository.findAll();
        return allComments.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .map(commentMapper::toResponse)
                .orElseThrow(() -> new CreatorNotFoundException(id));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }
    }

    private OutTopicDTO handleUpdate(CommentRequestTo dto) {
        Comment currComment = commentRepository.findAll().stream()
                .filter(comment -> comment.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(dto.getId()));

        currComment.setContent(dto.getContent());
        Comment updatedComment = commentRepository.save(currComment);
        return new OutTopicDTO(commentMapper.toResponse(updatedComment), "APPROVE");
    }

    public CommentResponseTo update(CommentRequestTo dto) {
        Comment currComment = commentRepository.findAll().stream()
                .filter(comment -> comment.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(dto.getId()));

        currComment.setContent(dto.getContent());
        Comment updatedComment = commentRepository.save(currComment);
        return commentMapper.toResponse(updatedComment);
    }

    private OutTopicDTO handleDelete(Long id) {
        Comment currComment = commentRepository.findAll().stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(id));
        if (currComment != null) {
            commentRepository.delete(currComment);
        } else {
            throw new CreatorNotFoundException(id);
        }
        return new OutTopicDTO(commentMapper.toResponse(currComment), "APPROVE");
    }

}
