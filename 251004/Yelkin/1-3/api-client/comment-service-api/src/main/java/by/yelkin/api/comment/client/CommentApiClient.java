package by.yelkin.api.comment.client;

import by.yelkin.api.comment.api.CommentApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "comment-api-client", url = "${app.clients.comment.url}")
public interface CommentApiClient extends CommentApi {
}
