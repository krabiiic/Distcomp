package com.example.modulediscussion.service;

import com.example.modulediscussion.entity.Reaction;
//import com.example.modulediscussion.entity.Tweet;
import com.example.modulediscussion.exception.MyException;
import com.example.modulediscussion.mapper.ReactionMapper;
import com.example.modulediscussion.repository.ReactionRepository;
//import com.example.modulediscussion.repository.TweetRepository;
import com.example.modulepublisher.dto.ReactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionMapper reactionMapper;
    private final ReactionRepository reactionRepository;
    private final RestTemplate restTemplate;


    public ReactionDTO createReaction(ReactionDTO tweetDTO){
        Reaction tweet = reactionMapper.toReaction(tweetDTO);
        //Tweet tweet1 = tweetRepository.findTweetById(tweet.getTweetId()).orElseThrow(() -> new MyException("aaaaaa"));
        reactionRepository.save(tweet);
        ReactionDTO dto = reactionMapper.toReactionDTO(tweet);
        return  dto;
    }

    public ReactionDTO deleteReaction(int id) throws Exception {

        Optional<Reaction> ouser = reactionRepository.findAuthorById(id);
        Reaction user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        ReactionDTO dto = reactionMapper.toReactionDTO(user);
        reactionRepository.delete(user);
        return  dto;
    }

    public ReactionDTO getReaction(int id){
        Optional<Reaction> ouser = reactionRepository.findAuthorById(id);
        /*if(ouser.isEmpty()){
            ReactionDTO user = restTemplate.getForObject("http://localhost:24110//api/v1.0/reactions/"+ id, ReactionDTO.class);
            if (user!=null){
                return  user;
            }
        }*/



        Reaction user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        ReactionDTO dto = reactionMapper.toReactionDTO(user);
        return  dto;
    }

    public List<ReactionDTO> getReactions(){
        List<Reaction> users = reactionRepository.findAll();
        List<ReactionDTO> dtos = reactionMapper.toReactionDTOList(users);
        return  dtos;
    }

    public ReactionDTO updateReaction(ReactionDTO userDTO){
        Reaction tweet = reactionMapper.toReaction(userDTO);
        reactionRepository.save(tweet);
        ReactionDTO dto = reactionMapper.toReactionDTO(tweet);
        return  dto;
    }

}
