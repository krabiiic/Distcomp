package publisher.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient postWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:24130/api/v1.0/posts")
                .build();
    }
}
