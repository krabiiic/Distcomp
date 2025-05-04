package com.example.modulepublisher.service;
import com.example.modulepublisher.dto.IssueDTO;
import com.example.modulepublisher.entity.Issue;
import com.example.modulepublisher.entity.Author;
import com.example.modulepublisher.mapper.IssueMapper;
import com.example.modulepublisher.repository.AuthorRepository;
import com.example.modulepublisher.repository.IssueRepository;
import com.example.modulepublisher.repository.redis.IssueRedisRepository;
import com.example.modulepublisher.exception.DublExeption;
import com.example.modulepublisher.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueMapper tweetMapper;
    private final IssueRepository tweetRepository;
    private final AuthorRepository userRepository;
    private final IssueRedisRepository tweetRedisRepository;

    public IssueDTO createIssue(IssueDTO tweetDTO){
        Issue tweet = tweetMapper.toIssue(tweetDTO);
        Optional<Issue> odubl = tweetRepository.findIssueByTitle(tweet.getTitle());
        if(odubl.isPresent()) {
            throw new DublExeption("aaaaa");
        }
        Author user = userRepository.findAuthorById(tweet.getAuthorId()).orElseThrow(() -> new MyException("aaaaaa"));
        tweetRepository.save(tweet);
        IssueDTO dto = tweetMapper.toIssueDTO(tweet);
        tweetDTO.setId(dto.getId());
        tweetRedisRepository.save(tweetDTO);
        return  dto;

    }

    public IssueDTO deleteIssue(int id) throws Exception {
        tweetRedisRepository.deleteById(String.valueOf(id));
        Optional<Issue> ouser = tweetRepository.findIssueById(id);
        Issue user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        IssueDTO dto = tweetMapper.toIssueDTO(user);
        tweetRepository.delete(user);
        return  dto;

    }

    public IssueDTO getIssue(int id){
        Optional<Issue> ouser = tweetRepository.findIssueById(id);
        Issue user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        IssueDTO dto = tweetMapper.toIssueDTO(user);
        return  dto;

    }

    public List<IssueDTO> getIssues(){
        List<Issue> users = tweetRepository.findAll();
        List<IssueDTO> dtos = tweetMapper.toIssueDTOLost(users);
        return  dtos;

    }

    public IssueDTO updateIssue(IssueDTO userDTO){
        Issue tweet = tweetMapper.toIssue(userDTO);
        tweetRepository.save(tweet);
        IssueDTO dto = tweetMapper.toIssueDTO(tweet);
        return  dto;

    }

}
