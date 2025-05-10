package publisher.services.implementations;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import publisher.kafka.KafkaClient;
import publisher.kafka.MessageData;
import publisher.mappers.PostMapper;
import publisher.modelDTOs.PostRequestTo;
import publisher.modelDTOs.PostResponseTo;
import publisher.services.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Service
public class DefaultPostService implements PostService {
    //@Qualifier("postMapper")
    public final PostMapper mapper;
    private final KafkaClient kafkaClient;
    private final WebClient webClient;
    //private final TweetRepository tweetRepository;

    public Mono<PostResponseTo> createPost(PostRequestTo postRequestTo) {
        UUID uuid = UUID.randomUUID();
        long id =  Math.abs(uuid.getMostSignificantBits());
        postRequestTo.setId(id);
        try {
            return Mono.just(kafkaClient.send(new MessageData(MessageData.Operation.CREATE, postRequestTo)).responseTOs().get(0));
        }catch (TimeoutException e) {
            return webClient.post()
                    .uri("/api/v1.0/posts")
                    .bodyValue(postRequestTo)
                    .retrieve()
                    .bodyToMono(PostResponseTo.class);
        }
    }

    public Mono<PostResponseTo> findPostById(Long id) {
        try {
            return Mono.just(kafkaClient.send(new MessageData(MessageData.Operation.GET_BY_ID, id)).responseTOs().get(0));
        }catch (TimeoutException e) {
            return webClient.get()
                    .uri("/api/v1.0/posts/{id}", id)
                    .retrieve()
                    .bodyToMono(PostResponseTo.class);
        }
    }

    public Mono<Void> deletePost(Long id) {
        try {
            kafkaClient.send(new MessageData(MessageData.Operation.DELETE_BY_ID, id));
        }catch (TimeoutException e) {
            webClient.delete()
                    .uri("/api/v1.0/posts/{id}", id)
                    .retrieve()
                    .bodyToMono(Void.class).block();
        }
        return Mono.empty();
    }

    public Flux<PostResponseTo> getPosts() {
        try {
            return Flux.fromIterable(kafkaClient.send(new MessageData(MessageData.Operation.GET_ALL)).responseTOs());
        }catch (TimeoutException e) {
            return webClient.get()
                    .uri("/api/v1.0/posts")
                    .retrieve()
                    .bodyToFlux(PostResponseTo.class);
        }
    }

    public PostResponseTo updatePost(PostRequestTo postRequestTo) {
        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.UPDATE, postRequestTo)).responseTOs().get(0);
        }catch (TimeoutException e) {
            return webClient.put()
                    .uri("/api/v1.0/posts")
                    .bodyValue(postRequestTo)
                    .retrieve()
                    .bodyToMono(PostResponseTo.class).block();
        }
    }
}

