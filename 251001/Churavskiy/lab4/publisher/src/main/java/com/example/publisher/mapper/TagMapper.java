package com.example.publisher.mapper;

import com.example.publisher.dto.TagRequestTo;
import com.example.publisher.dto.TagResponseTo;
import com.example.publisher.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagResponseTo toDTO(Tag tag);
    Tag toEntity(TagRequestTo tagRequestTo);
}
