package by.molchan.Publisher.utils.mappers;

import by.molchan.Publisher.DTOs.Requests.CommentRequestDTO;
import by.molchan.Publisher.DTOs.Responses.CommentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentsMapper {
    @Mapping(target = "id", expression = "java(commentRequestDTO.getId())")
    @Mapping(target = "articleId", expression = "java(commentRequestDTO.getArticleId())")
    @Mapping(target = "content", expression = "java(commentRequestDTO.getContent())")
   CommentResponseDTO toCommentResponse(CommentRequestDTO commentRequestDTO);
}
