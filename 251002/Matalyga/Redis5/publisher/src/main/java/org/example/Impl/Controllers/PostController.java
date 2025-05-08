package org.example.Impl.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.Exceptions.NotFoundException;
import org.example.Impl.DTO.PostRequestTo;
import org.example.Impl.DTO.PostResponseTo;
import org.example.Impl.Kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    private RestClient restClient;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer<String, PostResponseTo> kafkaConsumer;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private String uriBase = "http://localhost:24130/api/v1.0/posts";

    /*@GetMapping
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
    }*/

    @GetMapping
    public ResponseEntity<List<?>> getPosts(@RequestHeader HttpHeaders headers) {
        System.out.println("---GET POSTS" + headers.toString());
        return ResponseEntity.status(200).body(restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(List.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable Long id) throws NotFoundException {
        System.out.println("---GET POST");
        PostRequestTo postRequestTo = new PostRequestTo();
        postRequestTo.setId(id);
        postRequestTo.setMethod("GET");
        kafkaProducer.send(postRequestTo, "inTopic");
        return ResponseEntity.status(HttpStatus.OK).body(listenKafka());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) throws NotFoundException {
        System.out.println("---DELETE POST");
        PostRequestTo postRequestTo = new PostRequestTo();
        postRequestTo.setMethod("DELETE");
        postRequestTo.setId(id);
        kafkaProducer.send(postRequestTo, "InTopic");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<PostResponseTo> savePost(@RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String acceptLanguageHeader, @RequestBody PostRequestTo post) throws NotFoundException{
        System.out.println("-----POST POST" + post.toString());
        post.setMethod("POST");
        kafkaProducer.send(post, "InTopic");
        return ResponseEntity.status(HttpStatus.CREATED).body(listenKafka());
    }

    @PutMapping()
    public ResponseEntity<PostResponseTo> updatePost(@RequestHeader String acceptLanguageHeader, @RequestBody PostRequestTo post) throws NotFoundException{
        System.out.println("---UPDATE POST");
        post.setCountry(acceptLanguageHeader);
        post.setCountry(acceptLanguageHeader);
        post.setMethod("PUT");
        kafkaProducer.send(post, "InTopic");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(listenKafka());
    }

    @GetMapping("/byNews/{id}")
    public ResponseEntity<?> getPostByNewsId(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        return restClient.get()
                .uri(uriBase + "/byNews/" + id)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(ResponseEntity.class);
    }

    /*private PostResponseTo listenKafka() throws NotFoundException {
        ConsumerRecords<String, PostResponseTo> records = kafkaConsumer.poll(Duration.ofMillis(1000));
        System.out.println("---RECEIVES FROM 2: " + records.toString());
        for (ConsumerRecord<String, PostResponseTo> record : records) {

            String key = record.key();
            PostResponseTo value = record.value();
            if (value == null) {
                throw new NotFoundException("Not found", 40400L);
            }
            long offset = record.offset();
            int partition = record.partition();
            System.out.println("---Received message: key = " + key + ", value = " + value +
                    ", offset = " + offset + ", partition = " + partition);

            return value;
        }
        return null;
    }*/

    private PostResponseTo listenKafka() throws NotFoundException {
        ConsumerRecords<String, PostResponseTo> records = kafkaConsumer.poll(Duration.ofMillis(20000));
        if (records.isEmpty()) {
            System.out.println("---No records received");
            return new PostResponseTo();
        }

        System.out.println("---RECEIVES FROM 2: Number of records = " + records.count());
        for (ConsumerRecord<String, PostResponseTo> record : records) {
            String key = record.key();
            PostResponseTo value = record.value();

            if (value == null) {
                throw new NotFoundException("Not found", 40400L);
            }

            long offset = record.offset();
            int partition = record.partition();
            System.out.println("---Received message: key = " + key + ", value = " + value +
                    ", offset = " + offset + ", partition = " + partition);

            return value;
        }
        return null;
    }
}