package by.ryabchikov.tweet_service.service;

import by.ryabchikov.tweet_service.dto.tweet.TweetRequestTo;
import by.ryabchikov.tweet_service.dto.tweet.TweetResponseTo;
import by.ryabchikov.tweet_service.dto.tweet.TweetUpdateRequestTo;

import java.util.List;

public interface TweetService {
    TweetResponseTo create(TweetRequestTo tweetRequestTo);

    List<TweetResponseTo> readAll();

    TweetResponseTo readById(Long id);

    TweetResponseTo update(TweetUpdateRequestTo tweetUpdateRequestTo);

    void deleteById(Long id);
}
