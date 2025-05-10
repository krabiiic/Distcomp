package com.example.publisher.mapper;

import com.example.publisher.dto.TopicRequestTo;
import com.example.publisher.dto.TopicResponseTo;
import com.example.publisher.entity.Tag;
import com.example.publisher.entity.Topic;
import com.example.publisher.entity.User;
import com.example.publisher.service.TagService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class TopicMapper {

    @Autowired
    private TagService tagService;

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTags")  // Добавляем маппинг для тегов
    public abstract Topic toEntity(TopicRequestTo topicRequestTo);

    @Mapping(source = "user.id", target = "userId")
    public abstract TopicResponseTo toDTO(Topic topic);

    @Named("mapTags")
    Set<Tag> mapTags(Set<String> tagNames) {
        if (tagNames == null) return null;
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            tags.add(tagService.findOrCreateTag(tagName));  // Используем метод поиска или создания тегов
        }
        return tags;
    }

    @Named("mapUser")
    User mapUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
