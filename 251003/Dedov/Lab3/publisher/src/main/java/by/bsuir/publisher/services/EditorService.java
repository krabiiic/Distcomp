package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.EditorRequestDto;
import by.bsuir.publisher.dto.responses.EditorResponseDto;

import java.util.List;

public interface EditorService extends BaseService<EditorRequestDto, EditorResponseDto> {
    List<EditorResponseDto> readAll();
}
