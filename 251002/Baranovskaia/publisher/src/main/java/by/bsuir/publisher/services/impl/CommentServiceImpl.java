package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.dto.CommentActionDto;
import by.bsuir.publisher.dto.CommentActionTypeDto;
import by.bsuir.publisher.dto.requests.CommentRequestDto;
import by.bsuir.publisher.dto.responses.ErrorDto;
import by.bsuir.publisher.dto.responses.CommentResponseDto;
import by.bsuir.publisher.dto.responses.converters.CommentResponseConverter;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.exceptions.ServiceException;
import by.bsuir.publisher.repositories.CommentCacheRepository;
import by.bsuir.publisher.repositories.TopicRepository;
import by.bsuir.publisher.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(rollbackFor = {ServiceException.class})
public class CommentServiceImpl implements CommentService {
    private final ObjectMapper objectMapper;
    private final CommentResponseConverter commentResponseConverter;
    private final CommentCacheRepository commentCacheRepository;
    private final TopicRepository topicRepository;

    private final ReplyingKafkaTemplate<String, CommentActionDto, CommentActionDto> replyingKafkaTemplate;

    @Value("${topic.inTopic}")
    private String inTopic;

    @Value("${topic.outTopic}")
    private String outTopic;

    @KafkaListener(topics = "${topic.commentChangeTopic}")
    protected void receiveCommentChange(CommentActionDto commentActionDto) {
        switch (commentActionDto.getAction()) {
            case CREATE, UPDATE -> {
                CommentResponseDto commentResponseDto = objectMapper.
                        convertValue(commentActionDto.getData(), CommentResponseDto.class);
                commentCacheRepository.save(commentResponseConverter.fromDto(commentResponseDto));
            }
            case DELETE -> {
                Long id = Long.valueOf((String) commentActionDto.getData());
                commentCacheRepository.deleteById(id);
            }
        }
    }

    protected CommentActionDto sendCommentAction(CommentActionDto commentActionDto) {
        ProducerRecord<String, CommentActionDto> record = new ProducerRecord<>(inTopic, null,
                System.currentTimeMillis(), String.valueOf(commentActionDto.toString()),
                commentActionDto);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, outTopic.getBytes()));
        RequestReplyFuture<String, CommentActionDto, CommentActionDto> response = replyingKafkaTemplate.sendAndReceive(record);
        return response.orTimeout(1000, TimeUnit.MILLISECONDS).join().value();
    }

    @Override
    public CommentResponseDto create(@NonNull CommentRequestDto dto) throws ServiceException, NoEntityExistsException {
        topicRepository.findById(dto.getTopicId()).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException));
        //Synchronous, as response in tests shall contain created comment id, and if passed id is null,
        //asynchronous messaging is not appropriate(we won't be able to obtain generated id)
        CommentActionDto action = sendCommentAction(CommentActionDto.builder().
                action(CommentActionTypeDto.CREATE).
                data(dto).
                build());
        CommentResponseDto response = objectMapper.convertValue(action.getData(), CommentResponseDto.class);
        if (ObjectUtils.allNull(response.getId(), response.getTopicId(), response.getContent())) {
            throw new ServiceException(objectMapper.convertValue(action.getData(), ErrorDto.class));
        }
        commentCacheRepository.save(commentResponseConverter.fromDto(response));
        return response;
    }

    @Override
    public Optional<CommentResponseDto> read(@NonNull Long uuid) {
        return Optional.ofNullable(commentCacheRepository.findById(uuid).map(commentResponseConverter::toDto).orElseGet(() -> {
            CommentActionDto action = sendCommentAction(CommentActionDto.builder().
                    action(CommentActionTypeDto.READ).
                    data(String.valueOf(uuid)).
                    build());
            return objectMapper.convertValue(action.getData(), CommentResponseDto.class);
        }));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<CommentResponseDto> readAll() {
        return (List<CommentResponseDto>) ((List)sendCommentAction(CommentActionDto.builder().
                action(CommentActionTypeDto.READ_ALL).
                build()).getData()).stream().map(v -> objectMapper.convertValue(v, CommentResponseDto.class)).toList();
    }

    @Override
    public CommentResponseDto update(@NonNull CommentRequestDto dto) throws ServiceException {
        CommentActionDto action = sendCommentAction(CommentActionDto.builder().
                action(CommentActionTypeDto.UPDATE).
                data(dto).
                build());
        CommentResponseDto response = objectMapper.convertValue(action.getData(), CommentResponseDto.class);
        if (ObjectUtils.allNull(response.getId(), response.getContent(), response.getTopicId())) {
            throw new ServiceException(objectMapper.convertValue(action.getData(), ErrorDto.class));
        }
        commentCacheRepository.save(commentResponseConverter.fromDto(response));
        return response;
    }

    @Override
    public Long delete(@NonNull Long uuid) throws ServiceException {
        CommentActionDto action = sendCommentAction(CommentActionDto.builder().
                action(CommentActionTypeDto.DELETE).
                data(String.valueOf(uuid)).
                build());
        try {
            commentCacheRepository.deleteById(uuid);
            return Long.valueOf((Integer) action.getData());
        } catch (ClassCastException e) {
            throw new ServiceException(objectMapper.convertValue(action.getData(), ErrorDto.class));
        }
    }
}
