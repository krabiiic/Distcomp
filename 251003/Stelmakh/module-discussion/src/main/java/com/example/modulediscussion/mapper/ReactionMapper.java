package com.example.modulediscussion.mapper;

import org.mapstruct.Mapper;
import com.example.modulediscussion.entity.Reaction;
import com.example.modulepublisher.dto.ReactionDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toReaction(ReactionDTO messageDTO);
    ReactionDTO toReactionDTO(Reaction message);
    List<Reaction> toReactionList(List<ReactionDTO> messageDTOS);
    List<ReactionDTO>toReactionDTOList(List<Reaction> messages);
}
