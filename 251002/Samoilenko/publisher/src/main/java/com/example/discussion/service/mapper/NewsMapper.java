package com.example.discussion.service.mapper;

import com.example.discussion.dto.NewsRequestTo;
import com.example.discussion.dto.NewsResponseTo;
import com.example.discussion.model.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "markers", ignore = true)
    News toEntity(NewsRequestTo dto);
    @Mapping(source = "writer.id", target = "writerId")
    NewsResponseTo toDto(News entity);
    @Mapping(target = "markers", ignore = true)
    void updateEntityFromDto(NewsRequestTo dto, @MappingTarget News entity);
}