package by.yelkin.TopicService.mapper;

import by.yelkin.TopicService.dto.CommentRequestTo;
import by.yelkin.TopicService.dto.CommentResponseTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponseTo toPostResponse(CommentRequestTo commentRequestTo);
}