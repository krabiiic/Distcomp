package by.yelkin.commentservice.mapping;

import by.yelkin.api.comment.dto.CommentRq;
import by.yelkin.api.comment.dto.CommentRs;
import by.yelkin.api.comment.dto.CommentUpdateRq;
import by.yelkin.commentservice.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment fromDto(CommentRq rq);

    CommentRs toDto(Comment comment);

    List<CommentRs> toDtoList(Iterable<Comment> comments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentUpdateRq rq);
}
