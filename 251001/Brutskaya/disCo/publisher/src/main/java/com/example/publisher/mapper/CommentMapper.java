package com.example.publisher.mapper;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.discussion.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    Comment fromRequestToEntity(CommentRequestTo request);

    CommentResponseTo fromEntityToResponse(Comment entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Comment entity, CommentRequestTo request);
}

