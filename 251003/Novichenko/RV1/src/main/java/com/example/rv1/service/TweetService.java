package com.example.rv1.service;

import com.example.rv1.dto.TweetDTO;
import com.example.rv1.dto.UserDTO;
import com.example.rv1.entity.Tweet;
import com.example.rv1.entity.User;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.TweetMapper;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetService {
    private final TweetMapper tweetMapper;
    public TweetDTO createTweet(TweetDTO tweetDTO){
        Tweet tweet = tweetMapper.toTweet(tweetDTO);
        int amount = InMemoryStorage.tweets.size();
        tweet.setId(amount);
        InMemoryStorage.tweets.add(tweet);
        TweetDTO dto = tweetMapper.toTweetDTO(tweet);
        return  dto;
    }

    public TweetDTO deleteTweet(int id) throws Exception {
        int amount = InMemoryStorage.tweets.size();
        if (id >= amount){
            throw new MyException("aaaaaaaa");
        }
        Tweet user = InMemoryStorage.tweets.get(id);
        TweetDTO dto = tweetMapper.toTweetDTO(user);
        InMemoryStorage.tweets.remove(id);
        return  dto;
    }

    public TweetDTO getTweet(int id){
        Tweet user = InMemoryStorage.tweets.get(id);
        TweetDTO dto = tweetMapper.toTweetDTO(user);
        return  dto;
    }

    public List<TweetDTO> getTweets(){
        List<Tweet> users = InMemoryStorage.tweets;
        List<TweetDTO> dtos = tweetMapper.toTweetDTOLost(users);
        return  dtos;
    }

    public TweetDTO updateTweet(TweetDTO userDTO){
        Tweet user = tweetMapper.toTweet(userDTO);
        int id = user.getId();
        InMemoryStorage.tweets.set(id,user);
        TweetDTO dto = tweetMapper.toTweetDTO(user);
        return  dto;
    }

}
