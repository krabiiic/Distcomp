package com.example.rv1.mapper;

import com.example.rv1.dto.TweetDTO;
import com.example.rv1.entity.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TweetMapper {
   Tweet toTweet(TweetDTO tweetDTO);
   TweetDTO toTweetDTO(Tweet tweet);
   List<Tweet> toTweetList(List<TweetDTO>tweetDTOS);
   List<TweetDTO> toTweetDTOLost(List<Tweet> tweets);
}
