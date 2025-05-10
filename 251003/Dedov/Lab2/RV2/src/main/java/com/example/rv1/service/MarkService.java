package com.example.rv1.service;

import com.example.rv1.dto.MarkDTO;
import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Mark;
import com.example.rv1.entity.Message;
import com.example.rv1.exception.ExceptionBadRequest;
import com.example.rv1.mapper.MarkMapper;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.repository.MarkRepository;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkMapper markMapper;
    private final MarkRepository markRepository;
    public MarkDTO createMark(MarkDTO tweetDTO){
        Mark tweet = markMapper.toMark(tweetDTO);
        markRepository.save(tweet);
        MarkDTO dto = markMapper.toMarkDTO(tweet);
        return  dto;
    }

    public MarkDTO deleteMark(int id) throws Exception {
        Optional<Mark> ouser = markRepository.findMarkById(id);
        Mark user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        MarkDTO dto = markMapper.toMarkDTO(user);
        markRepository.delete(user);
        return  dto;
    }

    public MarkDTO getMark(int id){
        Optional<Mark> ouser = markRepository.findMarkById(id);
        Mark user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        MarkDTO dto = markMapper.toMarkDTO(user);
        return  dto;
    }

    public List<MarkDTO> getMarks(){
        List<Mark> users = markRepository.findAll();
        List<MarkDTO> dtos = markMapper.toMarkDTOList(users);
        return  dtos;
    }

    public MarkDTO updateMark(MarkDTO userDTO){
        Mark tweet = markMapper.toMark(userDTO);
        markRepository.save(tweet);
        MarkDTO dto = markMapper.toMarkDTO(tweet);
        return  dto;
    }

}
