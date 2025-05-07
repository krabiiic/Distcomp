package com.example.rest.controller;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.dto.PostUpdate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class DiscussionClient {

    private final RestClient restClient;

    public DiscussionClient(RestClient restClient) {
        this.restClient = restClient;
    }

    // Получение всех постов
    public List<PostResponseTo> getAllPosts() {
        return restClient.get()
                .uri("/api/v1.0/posts")
                .retrieve()
                .body(List.class); // Spring автоматически преобразует JSON в List<PostResponseTo>
    }

    // Создание нового поста
    public PostResponseTo createPost(PostRequestTo requestTo) {
        return restClient.post()
                .uri("/api/v1.0/posts")
                .body(requestTo)
                .retrieve()
                .body(PostResponseTo.class);
    }

    // Получение поста по ID
    public PostResponseTo getPostById(Long id) {
        return restClient.get()
                .uri("/api/v1.0/posts/{id}", id)
                .retrieve()
                .body(PostResponseTo.class);
    }

    // Обновление поста
    public PostResponseTo updatePost(PostUpdate postUpdate) {
        return restClient.put()
                .uri("/api/v1.0/posts")
                .body(postUpdate)
                .retrieve()
                .body(PostResponseTo.class);
    }

    // Удаление поста
    public void deletePost(Long id) {
        restClient.delete()
                .uri("/api/v1.0/posts/{id}", id)
                .retrieve()
                .toBodilessEntity(); // Возвращаем пустой ответ
    }
}