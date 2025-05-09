package com.rita.discussion.mapper;

import com.rita.discussion.dto.TweetDTO;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.client.RestClient;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Named("MapperUtil")
@Component
@RequiredArgsConstructor
public class MapperUtil {

//
//    @Named("getTweetId")
//    public Long getTweetId(Long tweetId){
//        if(restClient
//                .get()
//                .uri("http://localhost:24110/api/v1.0/tweets/{id}",tweetId)
//                .retrieve().toEntity(TweetDTO.class).getStatusCode().is2xxSuccessful()){
//            return tweetId;
//        }
//        else{
//           throw new EntityNotFoundException("Tweet not found with id: " + tweetId);
//
//        }
//    }

}
