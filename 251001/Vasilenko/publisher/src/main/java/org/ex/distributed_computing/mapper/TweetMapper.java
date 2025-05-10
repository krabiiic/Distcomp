package org.ex.distributed_computing.mapper;

import java.util.List;
import org.ex.distributed_computing.dto.request.TweetRequestDTO;
import org.ex.distributed_computing.dto.response.TweetResponseDTO;
import org.ex.distributed_computing.model.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TweetMapper {

  @Mapping(source = "writerId", target = "writer.id")
  Tweet toEntity(TweetRequestDTO dto);

  @Mapping(source = "writer.id", target = "writerId")
  TweetResponseDTO toDto(Tweet tweet);

  List<TweetResponseDTO> toDtoList(List<Tweet> tweetList);
}

