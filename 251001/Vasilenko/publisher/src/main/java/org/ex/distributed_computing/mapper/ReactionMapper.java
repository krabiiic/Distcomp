package org.ex.distributed_computing.mapper;

import java.util.List;
import org.ex.distributed_computing.dto.request.ReactionRequestDTO;
import org.ex.distributed_computing.dto.response.ReactionResponseDTO;
import org.ex.distributed_computing.model.Reaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

  @Mapping(source = "tweetId", target = "tweet.id")
  Reaction toEntity(ReactionRequestDTO dto);

  @Mapping(source = "tweet.id", target = "tweetId")
  ReactionResponseDTO toDto(Reaction reaction);

  ReactionResponseDTO convert(ReactionRequestDTO dto);

  List<ReactionResponseDTO> toDtoList(List<Reaction> reactions);
}

