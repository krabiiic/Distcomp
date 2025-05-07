package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.CreatorRequestDto;
import by.bsuir.publisher.dto.responses.CreatorResponseDto;

import java.util.List;

public interface CreatorService extends BaseService<CreatorRequestDto, CreatorResponseDto> {
    List<CreatorResponseDto> readAll();
}
