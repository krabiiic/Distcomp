package com.publisher.rvlab4_publisher.mapper;

import com.publisher.rvlab4_publisher.dto.NewsRequestTo;
import com.publisher.rvlab4_publisher.dto.NewsResponseTo;
import com.publisher.rvlab4_publisher.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    News toEntity(NewsRequestTo newsRequestTo);

    NewsResponseTo toResponseDto(News news);

    News toEntity(NewsResponseTo newsResponseTo);

    NewsRequestTo toRequestDto(News news);
}
