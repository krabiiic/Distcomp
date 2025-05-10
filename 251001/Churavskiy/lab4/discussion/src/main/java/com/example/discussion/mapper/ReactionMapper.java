package com.example.discussion.mapper;

import com.example.discussion.dto.ReactionRequestTo;
import com.example.discussion.dto.ReactionResponseTo;
import com.example.discussion.entity.Reaction;
import com.example.discussion.entity.ReactionKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface ReactionMapper {
    ReactionMapper INSTANCE = Mappers.getMapper(ReactionMapper.class);

    @Mapping(target = "country", source = "id.country")
    @Mapping(target = "topicId", source = "id.topicId")
    @Mapping(target = "id", source = "id.id")
    ReactionResponseTo toDto(Reaction reaction);

    @Mapping(target = "id", source = ".", qualifiedByName = "mapKey")
    Reaction toEntity(ReactionRequestTo dto);

    @Named("mapKey")
    default ReactionKey mapKey(ReactionRequestTo dto) {
        String country = dto.country() == null ? "default" : dto.country();
        Long id = (dto.id() != null) ? dto.id() : generateId();
        return new ReactionKey(country, dto.topicId(), id);
    }

    private Long generateId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }
}
