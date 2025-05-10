package com.publisher.rvlab4_publisher.mapper;

import com.publisher.rvlab4_publisher.dto.AuthorRequestTo;
import com.publisher.rvlab4_publisher.dto.AuthorResponseTo;
import com.publisher.rvlab4_publisher.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author toEntity(AuthorRequestTo authorRequestTo);

    AuthorResponseTo toResponseDto(Author author);

    Author toEntity(AuthorResponseTo authorResponseTo);

    AuthorRequestTo toRequestDto(Author author);
}
