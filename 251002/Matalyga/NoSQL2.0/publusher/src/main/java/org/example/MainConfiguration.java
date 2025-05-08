package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MainConfiguration {
    @Bean
    public RestClient restTemplate() {
        return RestClient.create();
    }
}
