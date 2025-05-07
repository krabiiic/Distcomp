package com.example.lab1.mapper;

import com.example.lab1.dto.TopicRequestTo;
import com.example.lab1.dto.TopicResponseTo;
import com.example.lab1.entity.Tag;
import com.example.lab1.entity.Topic;
import com.example.lab1.entity.User;
import com.example.lab1.service.TagService;  // Импортируем сервис для поиска тегов
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
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
