package by.kardychka.Publisher.utils.mappers;

import by.kardychka.Publisher.DTOs.Requests.PostRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.PostResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostsMapper {
    @Mapping(target = "id", expression = "java(postRequestDTO.getId())")
    @Mapping(target = "newsId", expression = "java(postRequestDTO.getNewsId())")
    @Mapping(target = "content", expression = "java(postRequestDTO.getContent())")
    PostResponseDTO toPostResponse(PostRequestDTO postRequestDTO);


}