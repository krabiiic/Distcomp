package by.ryabchikov.tweet_service.mapper;

import by.ryabchikov.tweet_service.dto.tweet.TweetRequestTo;
import by.ryabchikov.tweet_service.dto.tweet.TweetResponseTo;
import by.ryabchikov.tweet_service.entity.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TweetMapper {

    @Mapping(target = "creatorId", source = "creator.id")
    TweetResponseTo toTweetResponseTo(Tweet tweet);

    @Mapping(target = "creator.id", source = "creatorId")
    Tweet toTweet(TweetRequestTo tweetRequestTo);
}
