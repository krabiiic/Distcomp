package publisher.services;

import publisher.modelDTOs.PostRequestTo;
import publisher.modelDTOs.PostResponseTo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostService {
    public Mono<PostResponseTo> createPost(PostRequestTo postRequestTo);

    public Mono<PostResponseTo> findPostById(Long id);

    public Mono<Void> deletePost(Long id);

    public Flux<PostResponseTo> getPosts();

    public PostResponseTo updatePost(PostRequestTo postRequestTo);
}
