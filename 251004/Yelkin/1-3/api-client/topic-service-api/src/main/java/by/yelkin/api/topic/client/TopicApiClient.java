package by.yelkin.api.topic.client;

import by.yelkin.api.topic.api.TopicApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "topic-api-client", url = "${app.clients.topic.url}")
public interface TopicApiClient extends TopicApi {
}
