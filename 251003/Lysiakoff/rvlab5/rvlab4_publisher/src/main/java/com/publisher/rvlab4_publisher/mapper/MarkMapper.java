package com.publisher.rvlab4_publisher.mapper;

import com.publisher.rvlab4_publisher.dto.MarkRequestTo;
import com.publisher.rvlab4_publisher.dto.MarkResponseTo;
import com.publisher.rvlab4_publisher.entity.Mark;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarkMapper {

    MarkMapper INSTANCE = Mappers.getMapper(MarkMapper.class);

    Mark toEntity(MarkRequestTo markRequestTo);

    MarkResponseTo toResponseDto(Mark mark);

    Mark toEntity(MarkResponseTo markResponseTo);

    MarkRequestTo toRequestDto(Mark mark);
}
