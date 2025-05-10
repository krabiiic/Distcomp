package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.MarkRequestDto;
import by.bsuir.publisher.dto.responses.MarkResponseDto;

import java.util.List;

public interface MarkService extends BaseService<MarkRequestDto, MarkResponseDto> {
    List<MarkResponseDto> readAll();
}
