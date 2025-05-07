package com.example.lab1.mapper;

import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import com.example.lab1.entity.Reaction;
import com.example.lab1.entity.Topic;
import com.example.lab1.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReactionMapper {
    ReactionMapper INSTANCE = Mappers.getMapper(ReactionMapper.class);

    @Mapping(source = "topic.id", target = "topicId")
    ReactionResponseTo toDTO(Reaction reaction);

    @Mapping(source = "topicId", target = "topic", qualifiedByName = "mapTopic")
    Reaction toEntity(ReactionRequestTo reactionRequestTo);

    @Named("mapTopic")
    default Topic mapTopic(Long topicId) {
        if (topicId == null) return null;
        Topic topic = new Topic();
        topic.setId(topicId);
        return topic;
    }
}
