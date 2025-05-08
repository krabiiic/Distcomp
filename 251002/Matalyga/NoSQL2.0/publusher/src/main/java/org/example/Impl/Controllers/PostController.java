package org.example.Impl.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.example.Impl.DTO.PostRequestTo;
import org.example.Impl.DTO.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    private RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private String uriBase = "http://localhost:24130/api/v1.0/posts";

    @GetMapping
    public ResponseEntity<List<?>> getPosts(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.status(200).body(restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(List.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable Long id) {
        System.out.println("get:");
        return ResponseEntity.status(200).body(restClient.get()
                .uri(uriBase + "/" + id)
                .retrieve()
                .body(PostResponseTo.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(restClient.delete()
                .uri(uriBase + "/" + id)
                .retrieve()
                .toBodilessEntity().getBody());
    }

    private ResponseEntity<PostResponseTo> PutMock;
    @PostMapping
    public ResponseEntity<PostResponseTo> savePost(@RequestHeader HttpHeaders headers, @RequestBody PostRequestTo post) {
        ResponseEntity entity = ResponseEntity.status(201).body(restClient.post()
                .uri(uriBase)
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(48)
                .body(post)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(PostResponseTo.class));
        PutMock = entity;
        return entity;
    }

    @PutMapping()
    public ResponseEntity<PostResponseTo> updatePost(@RequestHeader HttpHeaders headers,@RequestBody PostRequestTo post) {
        ResponseEntity<PostResponseTo> e = ResponseEntity.status(200).body(restClient.put()
                .uri(uriBase)
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(60)
                .body(post)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(PostResponseTo.class));
        return e;
    }

    @GetMapping("/byIssue/{id}")
    public ResponseEntity<?> getEditorByIssueId(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        return restClient.get()
                .uri(uriBase + "/byIssue/" + id)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(ResponseEntity.class);
    }
}