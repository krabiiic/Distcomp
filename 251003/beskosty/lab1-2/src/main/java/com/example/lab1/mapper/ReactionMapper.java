package com.example.lab1.mapper;

import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import com.example.lab1.model.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toEntity(ReactionRequestTo dto);
    ReactionResponseTo toDto(Reaction entity);
}
