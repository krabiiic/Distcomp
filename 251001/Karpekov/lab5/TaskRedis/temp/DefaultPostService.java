package publisher.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import publisher.modelDTOs.PostRequestTo;
import publisher.modelDTOs.PostResponseTo;
import publisher.services.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class DefaultPostService implements PostService {

    private final WebClient postWebClient;

    public Mono<PostResponseTo> createPost(PostRequestTo postRequestTo) {
        return postWebClient.post()
                .uri("")
                .bodyValue(postRequestTo)
                .retrieve()
                .bodyToMono(PostResponseTo.class);
    }

    public Mono<PostResponseTo> findPostById(Long id) {
        return postWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(PostResponseTo.class);
    }

    public Mono<Void> deletePost(Long id) {
        return postWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<PostResponseTo> getPosts() {
        return postWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(PostResponseTo.class);
    }

    public PostResponseTo updatePost(PostRequestTo postRequestTo) {
        return postWebClient.put()
                .uri("")
                .bodyValue(postRequestTo)
                .retrieve()
                .bodyToMono(PostResponseTo.class)
                .block();
    }
}

