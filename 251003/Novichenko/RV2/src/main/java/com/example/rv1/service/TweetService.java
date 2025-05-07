package com.example.rv1.service;

import com.example.rv1.dto.TweetDTO;
import com.example.rv1.dto.UserDTO;
import com.example.rv1.entity.Tweet;
import com.example.rv1.entity.User;
import com.example.rv1.exception.DublExeption;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.TweetMapper;
import com.example.rv1.repository.TweetRepository;
import com.example.rv1.repository.UserRepository;
import com.example.rv1.storage.InMemoryStorage;
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
