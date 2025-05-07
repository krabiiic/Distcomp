package com.example.rv1.service;

import com.example.rv1.dto.LabelDTO;
import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Label;
import com.example.rv1.entity.Message;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.LabelMapper;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelMapper labelMapper;
    public LabelDTO createLabel(LabelDTO tweetDTO){
        Label tweet = labelMapper.toLabel(tweetDTO);
        int amount = InMemoryStorage.labels.size();
        tweet.setId(amount);
        InMemoryStorage.labels.add(tweet);
        LabelDTO dto = labelMapper.toLabelDTO(tweet);
        return  dto;
    }

    public LabelDTO deleteLabel(int id) throws Exception {
        int amount = InMemoryStorage.labels.size();
        if (id >= amount){
            throw new MyException("aaaaaaaa");
        }
        Label user = InMemoryStorage.labels.get(id);
        LabelDTO dto = labelMapper.toLabelDTO(user);
        InMemoryStorage.labels.remove(id);
        return  dto;
    }

    public LabelDTO getLabel(int id){
        Label user = InMemoryStorage.labels.get(id);
        LabelDTO dto = labelMapper.toLabelDTO(user);
        return  dto;
    }

    public List<LabelDTO> getLabels(){
        List<Label> users = InMemoryStorage.labels;
        List<LabelDTO> dtos = labelMapper.toLabelDTOList(users);
        return  dtos;
    }

    public LabelDTO updateLabel(LabelDTO userDTO){
        Label user = labelMapper.toLabel(userDTO);
        int id = user.getId();
        InMemoryStorage.labels.set(id,user);
        LabelDTO dto = labelMapper.toLabelDTO(user);
        return  dto;
    }

}
