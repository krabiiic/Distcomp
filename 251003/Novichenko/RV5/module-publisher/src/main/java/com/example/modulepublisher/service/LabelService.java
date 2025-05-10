package com.example.modulepublisher.service;

import com.example.modulepublisher.dto.LabelDTO;
import com.example.modulepublisher.dto.MessageDTO;
import com.example.modulepublisher.entity.Label;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.LabelMapper;
import com.example.modulepublisher.repository.LabelRepository;
import com.example.modulepublisher.repository.redis.LabelRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelMapper labelMapper;
    private final LabelRepository labelRepository;
    private final LabelRedisRepository labelRedisRepository;
    public LabelDTO createLabel(LabelDTO tweetDTO){
        Label tweet = labelMapper.toLabel(tweetDTO);
        labelRepository.save(tweet);
        LabelDTO dto = labelMapper.toLabelDTO(tweet);
        tweetDTO.setId(dto.getId());
        labelRedisRepository.save(tweetDTO);
        return  dto;


    }

    public LabelDTO deleteLabel(int id) throws Exception {
        labelRedisRepository.deleteById(String.valueOf(id));
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
