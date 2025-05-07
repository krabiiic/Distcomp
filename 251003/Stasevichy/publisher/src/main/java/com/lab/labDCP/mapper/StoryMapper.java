package com.lab.labDCP.mapper;

import com.lab.labDCP.dto.StoryRequestTo;
import com.lab.labDCP.dto.StoryResponseTo;
import com.lab.labDCP.entity.Story;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoryMapper {

    StoryMapper INSTANCE = Mappers.getMapper(StoryMapper.class);

    Story toEntity(StoryRequestTo storyRequestTo);

    StoryResponseTo toResponseDto(Story story);

    Story toEntity(StoryResponseTo storyResponseTo);

    StoryRequestTo toRequestDto(Story story);
}
