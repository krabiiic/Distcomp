package com.example.rv1.service;

import com.example.rv1.dto.LabelDTO;
import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Label;
import com.example.rv1.entity.Message;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.LabelMapper;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.repository.LabelRepository;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelMapper labelMapper;
    private final LabelRepository labelRepository;
    public LabelDTO createLabel(LabelDTO tweetDTO){
        Label tweet = labelMapper.toLabel(tweetDTO);
        labelRepository.save(tweet);
        LabelDTO dto = labelMapper.toLabelDTO(tweet);
        return  dto;
    }

    public LabelDTO deleteLabel(int id) throws Exception {
        Optional<Label> ouser = labelRepository.findUserById(id);
        Label user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        LabelDTO dto = labelMapper.toLabelDTO(user);
        labelRepository.delete(user);
        return  dto;
    }

    public LabelDTO getLabel(int id){
        Optional<Label> ouser = labelRepository.findUserById(id);
        Label user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        LabelDTO dto = labelMapper.toLabelDTO(user);
        return  dto;
    }

    public List<LabelDTO> getLabels(){
        List<Label> users = labelRepository.findAll();
        List<LabelDTO> dtos = labelMapper.toLabelDTOList(users);
        return  dtos;
    }

    public LabelDTO updateLabel(LabelDTO userDTO){
        Label tweet = labelMapper.toLabel(userDTO);
        labelRepository.save(tweet);
        LabelDTO dto = labelMapper.toLabelDTO(tweet);
        return  dto;
    }

}
