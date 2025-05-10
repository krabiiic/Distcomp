package by.yelkin.CommentService.mapper;

import by.yelkin.CommentService.dto.CommentRequestTo;
import by.yelkin.CommentService.dto.CommentResponseTo;
import by.yelkin.CommentService.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toEntity(CommentRequestTo request);
    CommentResponseTo toResponse(Comment comment);
    List<CommentResponseTo> toPostResponseList(Iterable<Comment> comments);

}