package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.ReactionDTO;
import com.example.modulepublisher.entity.Reaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toReaction(ReactionDTO reactionDTO);
    ReactionDTO toReactionDTO(Reaction reaction);
    List<Reaction> toReactionList(List<ReactionDTO> reactionDTOS);
    List<ReactionDTO>toReactionDTOList(List<Reaction> reactions);
}
