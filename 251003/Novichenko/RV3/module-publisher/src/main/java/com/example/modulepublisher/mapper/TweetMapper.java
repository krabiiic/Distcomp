package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.TweetDTO;
import com.example.modulepublisher.entity.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TweetMapper {
   Tweet toTweet(TweetDTO tweetDTO);
   TweetDTO toTweetDTO(Tweet tweet);
   List<Tweet> toTweetList(List<TweetDTO>tweetDTOS);
   List<TweetDTO> toTweetDTOLost(List<Tweet> tweets);
}
