package com.rita.publisher.mapper;

import com.rita.publisher.dto.TweetCreateDTO;
import com.rita.publisher.dto.TweetUpdateDTO;
import com.rita.publisher.dto.TweetDTO;
import com.rita.publisher.model.Tweet;
import org.mapstruct.*;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        MapperUtil.class
})
public interface TweetMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdTime",ignore = true)
    @Mapping(target = "modifiedTime",ignore = true)
    @Mapping(target = "stickers",qualifiedByName = {"MapperUtil", "getStickers"},source = "stickers", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "writer",qualifiedByName = {"MapperUtil", "getWriter"},source = "writerId")
    Tweet toTweet(TweetCreateDTO TweetCreateDTO);

    @Mapping(target = "writerId",qualifiedByName = {"MapperUtil", "getWriterId"},source = "writer")
    TweetDTO toTweetDTO(Tweet tweet);

    @Mapping(target = "stickers",ignore = true)
    @Mapping(target = "createdTime",ignore = true)
    @Mapping(target = "modifiedTime",ignore = true)
    @Mapping(target = "writer",qualifiedByName = {"MapperUtil", "getWriter"},source = "writerId")

    Tweet toTweet(TweetUpdateDTO TweetUpdateDTO);

}
