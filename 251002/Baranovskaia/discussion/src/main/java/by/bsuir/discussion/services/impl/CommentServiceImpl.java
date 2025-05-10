package by.bsuir.discussion.services.impl;

import by.bsuir.discussion.domain.Comment;
import by.bsuir.discussion.dto.CommentActionDto;
import by.bsuir.discussion.dto.CommentActionTypeDto;
import by.bsuir.discussion.dto.requests.CommentRequestDto;
import by.bsuir.discussion.dto.requests.converters.CommentRequestConverter;
import by.bsuir.discussion.dto.responses.CommentResponseDto;
import by.bsuir.discussion.dto.responses.converters.CollectionCommentResponseConverter;
import by.bsuir.discussion.dto.responses.converters.CommentResponseConverter;
import by.bsuir.discussion.exceptions.EntityExistsException;
import by.bsuir.discussion.exceptions.ErrorDto;
import by.bsuir.discussion.exceptions.Messages;
import by.bsuir.discussion.exceptions.NoEntityExistsException;
import by.bsuir.discussion.repositories.CommentRepository;
import by.bsuir.discussion.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(rollbackFor = {EntityExistsException.class, NoEntityExistsException.class})
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentRequestConverter commentRequestConverter;
    private final CommentResponseConverter commentResponseConverter;
    private final CollectionCommentResponseConverter collectionCommentResponseConverter;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, CommentActionDto> kafkaCommentActionTemplate;

    @Value("${topic.commentChangeTopic}")
    private String commentChangeTopic;

    private CommentService commentService;

    @Autowired
    public void setCommentService(@Lazy CommentService commentService) {
        this.commentService = commentService;
    }

    @KafkaListener(topics = "${topic.inTopic}")
    @SendTo
    protected CommentActionDto receive(CommentActionDto commentActionDto) {
        System.out.println("Received comment: " + commentActionDto);
        switch (commentActionDto.getAction()) {
            case CREATE -> {
                try {
                    CommentRequestDto commentRequest = objectMapper.convertValue(commentActionDto.getData(),
                            CommentRequestDto.class);
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.CREATE).
                            data(commentService.create(commentRequest)).
                            build();
                } catch (EntityExistsException e) {
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.CREATE).
                            data(ErrorDto.builder().
                                    code(HttpStatus.BAD_REQUEST.value() + "00").
                                    message(Messages.EntityExistsException).
                                    build()).
                            build();
                }
            }
            case READ -> {
                Long id = Long.valueOf((String) commentActionDto.getData());
                return CommentActionDto.builder().
                        action(CommentActionTypeDto.READ).
                        data(commentService.read(id)).
                        build();
            }
            case READ_ALL -> {
                return CommentActionDto.builder().
                        action(CommentActionTypeDto.READ_ALL).
                        data(commentService.readAll()).
                        build();
            }
            case UPDATE -> {
                try {
                    CommentRequestDto commentRequest = objectMapper.convertValue(commentActionDto.getData(),
                            CommentRequestDto.class);
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.UPDATE).
                            data(commentService.update(commentRequest)).
                            build();
                } catch (NoEntityExistsException e) {
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.UPDATE).
                            data(ErrorDto.builder().
                                    code(HttpStatus.BAD_REQUEST.value() + "00").
                                    message(Messages.NoEntityExistsException).
                                    build()).
                            build();
                }
            }
            case DELETE -> {
                try {
                    Long id = Long.valueOf((String) commentActionDto.getData());
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.DELETE).
                            data(commentService.delete(id)).
                            build();
                } catch (NoEntityExistsException e) {
                    return CommentActionDto.builder().
                            action(CommentActionTypeDto.DELETE).
                            data(ErrorDto.builder().
                                    code(HttpStatus.BAD_REQUEST.value() + "00").
                                    message(Messages.NoEntityExistsException).
                                    build()).
                            build();
                }
            }
        }
        return commentActionDto;
    }

    @Override
    @Validated
    public CommentResponseDto create(@Valid @NonNull CommentRequestDto dto) throws EntityExistsException {
        Optional<Comment> comment = dto.getId() == null ? Optional.empty() : commentRepository.findCommentById(dto.getId());
        if (comment.isEmpty()) {
            Comment entity = commentRequestConverter.fromDto(dto);
            if (dto.getId() == null) {
                entity.setId((long) (Math.random() * 2_000_000_000L) + 1);
            }
            CommentResponseDto commentResponseDto = commentResponseConverter.toDto(commentRepository.save(entity));
            CommentActionDto commentActionDto = CommentActionDto.builder().
                    action(CommentActionTypeDto.CREATE).
                    data(commentResponseDto).
                    build();
            ProducerRecord<String, CommentActionDto> record = new ProducerRecord<>(commentChangeTopic, null,
                    System.currentTimeMillis(), String.valueOf(commentActionDto.toString()),
                    commentActionDto);
            kafkaCommentActionTemplate.send(record);
            return commentResponseDto;
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<CommentResponseDto> read(@NonNull Long id) {
        return commentRepository.findCommentById(id).flatMap(user -> Optional.of(
                commentResponseConverter.toDto(user)));
    }

    @Override
    @Validated
    public CommentResponseDto update(@Valid @NonNull CommentRequestDto dto) throws NoEntityExistsException {
        Optional<Comment> comment = dto.getId() == null || commentRepository.findCommentByTopicIdAndId(
                dto.getTopicId(), dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(commentRequestConverter.fromDto(dto));
        CommentResponseDto commentResponseDto = commentResponseConverter.toDto(commentRepository.save(comment.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
        CommentActionDto commentActionDto = CommentActionDto.builder().
                action(CommentActionTypeDto.UPDATE).
                data(commentResponseDto).
                build();
        ProducerRecord<String, CommentActionDto> record = new ProducerRecord<>(commentChangeTopic, null,
                System.currentTimeMillis(), String.valueOf(commentActionDto.toString()),
                commentActionDto);
        kafkaCommentActionTemplate.send(record);
        return commentResponseDto;
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Comment> comment = commentRepository.findCommentById(id);
        commentRepository.deleteCommentByTopicIdAndId(comment.map(Comment::getTopicId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)), comment.map(Comment::getId).
                orElseThrow(() -> new NoEntityExistsException(Messages.NoEntityExistsException)));
        CommentActionDto commentActionDto = CommentActionDto.builder().
                action(CommentActionTypeDto.DELETE).
                data(String.valueOf(id)).
                build();
        ProducerRecord<String, CommentActionDto> record = new ProducerRecord<>(commentChangeTopic, null,
                System.currentTimeMillis(), String.valueOf(commentActionDto.toString()),
                commentActionDto);
        kafkaCommentActionTemplate.send(record);
        return comment.get().getId();
    }

    @Override
    public List<CommentResponseDto> readAll() {
        return collectionCommentResponseConverter.toListDto(commentRepository.findAll());
    }
}
