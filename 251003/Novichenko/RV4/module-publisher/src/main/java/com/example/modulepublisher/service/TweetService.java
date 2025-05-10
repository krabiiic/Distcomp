package com.example.modulepublisher.service;

import com.example.modulepublisher.dto.TweetDTO;
import com.example.modulepublisher.entity.Tweet;
import com.example.modulepublisher.entity.User;
import com.example.modulepublisher.exception.DublExeption;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.TweetMapper;
import com.example.modulepublisher.repository.TweetRepository;
import com.example.modulepublisher.repository.UserRepository;
import com.example.modulepublisher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetService {
    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    public TweetDTO createTweet(TweetDTO tweetDTO){
        Tweet tweet = tweetMapper.toTweet(tweetDTO);
        Optional<Tweet> odubl = tweetRepository.findTweetByTitle(tweet.getTitle());
        if(odubl.isPresent()) {
            throw new DublExeption("aaaaa");
        }
        User user = userRepository.findUserById(tweet.getUserId()).orElseThrow(() -> new MyException("aaaaaa"));
        tweetRepository.save(tweet);
        TweetDTO dto = tweetMapper.toTweetDTO(tweet);
        return  dto;

    }

    public TweetDTO deleteTweet(int id) throws Exception {
        Optional<Tweet> ouser = tweetRepository.findTweetById(id);
        Tweet user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        TweetDTO dto = tweetMapper.toTweetDTO(user);
        tweetRepository.delete(user);
        return  dto;

    }

    public TweetDTO getTweet(int id){
        Optional<Tweet> ouser = tweetRepository.findTweetById(id);
        Tweet user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        TweetDTO dto = tweetMapper.toTweetDTO(user);
        return  dto;

    }

    public List<TweetDTO> getTweets(){
        List<Tweet> users = tweetRepository.findAll();
        List<TweetDTO> dtos = tweetMapper.toTweetDTOLost(users);
        return  dtos;

    }

    public TweetDTO updateTweet(TweetDTO userDTO){
        Tweet tweet = tweetMapper.toTweet(userDTO);
        tweetRepository.save(tweet);
        TweetDTO dto = tweetMapper.toTweetDTO(tweet);
        return  dto;

    }

}
