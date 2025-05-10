package org.ikrotsyuk.bsuir.modulepublisher.mapper;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionResponseEventDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.ReactionDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ReactionRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ReactionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReactionMapper {
    @Mapping(source = "reactionId", target = "id")
    ReactionResponseDTO toResponseDTO(KafkaReactionResponseEventDTO reactionDTO);
    List<ReactionResponseDTO> toResponseDTOList(List<KafkaReactionResponseEventDTO> reactionDTOList);
    ReactionDTO toReactionDTO(ReactionRequestDTO reactionRequestDTO);
}
