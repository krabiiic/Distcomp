package com.example.modulepublisher.service;
import com.example.modulepublisher.dto.ReactionDTO;
import com.example.modulepublisher.entity.Reaction;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.ReactionMapper;
import com.example.modulepublisher.repository.ReactionRepository;
import com.example.modulepublisher.repository.IssueRepository;
import com.example.modulepublisher.repository.redis.ReactionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionMapper messageMapper;
    private final ReactionRepository messageRepository;
    private final IssueRepository tweetRepository;
    private final ReactionRedisRepository messageRedisRepository;

    public ReactionDTO createReaction(ReactionDTO tweetDTO){
        Reaction tweet = messageMapper.toReaction(tweetDTO);
        // Tweet tweet1 = tweetRepository.findTweetById(tweet.getTweetId()).orElseThrow(() -> new MyException("aaaaaa"));
        Reaction m = messageRepository.save(tweet);
        ReactionDTO dto = messageMapper.toReactionDTO(m);
        tweetDTO.setId(dto.getId());
        messageRedisRepository.save(tweetDTO);
        return  dto;

    }

    public ReactionDTO deleteReaction(int id) throws Exception {
        messageRedisRepository.deleteById(String.valueOf(id));
        Optional<Reaction> ouser = messageRepository.findAuthorById(id);
        Reaction user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        ReactionDTO dto = messageMapper.toReactionDTO(user);
        messageRepository.delete(user);
        return  dto;

    }

    public ReactionDTO getReaction(int id){
        Optional<Reaction> ouser = messageRepository.findAuthorById(id);
        Reaction user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        ReactionDTO dto = messageMapper.toReactionDTO(user);
        return  dto;

    }

    public List<ReactionDTO> getReactions(){
        List<Reaction> users = messageRepository.findAll();
        List<ReactionDTO> dtos = messageMapper.toReactionDTOList(users);
        return  dtos;

    }

    public ReactionDTO updateReaction(ReactionDTO userDTO){
        Reaction tweet = messageMapper.toReaction(userDTO);
        messageRepository.save(tweet);
        ReactionDTO dto = messageMapper.toReactionDTO(tweet);
        return  dto;


    }

}
