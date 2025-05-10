package by.yelkin.api.topic.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация клиента для работы с организациями из кабинета
 */
@Configuration
@EnableFeignClients("by.yelkin.api.topic")
public class TopicClientConfig {
}
