package by.kardychka.Publisher.utils.mappers;

import by.kardychka.Publisher.DTOs.Requests.NewsRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.NewsResponseDTO;
import by.kardychka.Publisher.models.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewssMapper {

    @Mapping(target = "creatorId", expression = "java(news.getCreator().getId())")
    @Mapping(target = "stickers", expression = "java(news.getStickers().stream().map(sticker -> sticker.getName()).toList())")
    NewsResponseDTO toNewsResponse(News news);

    List<NewsResponseDTO> toNewsResponseList(List<News> newss);

    @Mapping(target = "stickers", ignore = true)
    News toNews(NewsRequestDTO newsRequestDTO);
}
