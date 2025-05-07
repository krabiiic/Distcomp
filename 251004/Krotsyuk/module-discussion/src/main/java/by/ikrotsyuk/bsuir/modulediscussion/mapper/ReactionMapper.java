package by.ikrotsyuk.bsuir.modulediscussion.mapper;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionResponseEventDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.ReactionDTO;
import by.ikrotsyuk.bsuir.modulediscussion.dto.request.ReactionRequestDTO;
import by.ikrotsyuk.bsuir.modulediscussion.dto.response.ReactionResponseDTO;
import by.ikrotsyuk.bsuir.modulediscussion.entity.ReactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReactionMapper {
    ReactionResponseDTO toDTO(ReactionEntity reactionEntity);

    ReactionEntity toEntity(ReactionRequestDTO reactionRequestDTO);

    List<ReactionResponseDTO> toDTOList(List<ReactionEntity> reactionEntityList);

    @Mapping(target = "articleId", source = "articleId")
    ReactionEntity toEntity(ReactionDTO reactionDTO, Long articleId);

    @Mapping(target = "reactionId", source = "id")
    KafkaReactionResponseEventDTO toReactionResponse(ReactionEntity reactionEntity);

    List<KafkaReactionResponseEventDTO> toReactionResponseList(List<ReactionEntity> reactionEntityList);

    ReactionEntity toKafkaEntity(ReactionDTO reactionDTO);
}
