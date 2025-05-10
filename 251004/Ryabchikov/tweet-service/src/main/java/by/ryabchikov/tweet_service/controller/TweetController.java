package by.ryabchikov.tweet_service.controller;

import by.ryabchikov.tweet_service.dto.tweet.TweetRequestTo;
import by.ryabchikov.tweet_service.dto.tweet.TweetResponseTo;
import by.ryabchikov.tweet_service.dto.tweet.TweetUpdateRequestTo;
import by.ryabchikov.tweet_service.service.TweetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    public ResponseEntity<List<TweetResponseTo>> readAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tweetService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseTo> readById(@PathVariable("id") @Valid @NotNull Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tweetService.readById(id));
    }

    @PostMapping
    public ResponseEntity<TweetResponseTo> create(@Valid @RequestBody TweetRequestTo tweetRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tweetService.create(tweetRequestTo));
    }

    @PutMapping
    public ResponseEntity<TweetResponseTo> update(@Valid @RequestBody TweetUpdateRequestTo tweetUpdateRequestTo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tweetService.update(tweetUpdateRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @Valid @NotNull Long id) {
        tweetService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
