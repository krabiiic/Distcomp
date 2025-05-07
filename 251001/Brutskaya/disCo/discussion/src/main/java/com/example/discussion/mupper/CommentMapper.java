package com.example.discussion.mupper;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.discussion.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    Comment fromRequestToEntity(CommentRequestTo request);

    Comment fromResponseToEntity(CommentResponseTo response);

    CommentResponseTo fromEntityToResponse(Comment entity);

    void updateEntity(@MappingTarget Comment entity, CommentRequestTo request);

    CommentRequestTo fromEntityToRequest(Comment entity);
}

