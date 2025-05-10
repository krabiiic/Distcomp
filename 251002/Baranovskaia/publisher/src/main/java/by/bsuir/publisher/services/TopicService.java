package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.TopicRequestDto;
import by.bsuir.publisher.dto.responses.TopicResponseDto;

import java.util.List;

public interface TopicService extends BaseService<TopicRequestDto, TopicResponseDto> {
    List<TopicResponseDto> readAll();
}
