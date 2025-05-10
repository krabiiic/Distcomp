package com.example.rv1.controller;

import com.example.rv1.dto.TweetDTO;
import com.example.rv1.dto.UserDTO;
import com.example.rv1.entity.Tweet;
import com.example.rv1.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
@RequiredArgsConstructor
public class TweetController {
    private final TweetService tweetService;
    @PostMapping
    public ResponseEntity<TweetDTO> createUser(@Valid @RequestBody TweetDTO userDTO) {
        TweetDTO dto = tweetService.createTweet(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<TweetDTO> deleteUser(@PathVariable int id) throws Exception {
        TweetDTO dto = tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<TweetDTO> getUser(@PathVariable int id){
        TweetDTO dto = tweetService.getTweet(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<TweetDTO>> getUser(){
        List<TweetDTO> dto = tweetService.getTweets();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<TweetDTO> updateUser(@Valid @RequestBody TweetDTO userDTO){
        tweetService.updateTweet(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
