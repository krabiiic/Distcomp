package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Comment;
import by.bsuir.publisher.dto.responses.CommentResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentResponseConverter {
    CommentResponseDto toDto(Comment comment);
    Comment fromDto(CommentResponseDto commentResponseDto);
}
