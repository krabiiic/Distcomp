package com.dc.service;

import com.dc.exception.ServiceException;
import com.dc.kafka.MessageKafkaService;
import com.dc.mapper.KafkaMapper;
import com.dc.mapper.MessageMapper;
import com.dc.model.blo.Message;
import com.dc.model.dto.MessageRequestTo;
import com.dc.model.kafka.MessageKafka;
import com.dc.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {

    private WebClient webClient = WebClient.create("http://localhost:24130/api/v1.0/messages");
    private final Map<String, CompletableFuture<MessageKafka>> futures = new ConcurrentHashMap<>();

    @Autowired
    private MessageMapper mapper;

    @Autowired
    private KafkaMapper kafkaMapper;

    @Autowired
    private NewsRepository newsService;

    @Autowired
    private MessageKafkaService kafka;

    public Collection<MessageRequestTo> getAll() {
        Collection<MessageRequestTo> collection = webClient.get().retrieve().bodyToFlux(MessageRequestTo.class).collectList().block();
        return collection;
    }

    @Cacheable(value = "messages", key = "#id")
    public Message getById(Long id) {
        MessageRequestTo collection = webClient.get().uri("/" + id)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status == HttpStatus.OK) {
                        return response.bodyToMono(MessageRequestTo.class);
                    } else {
                        return Mono.error(new ServiceException("Ошибка", 404));
                    }
                })
                .block();
        if(collection==null)
            throw new ServiceException("Нет такого сообщения", 404);
        Message buf = mapper.mapToBlo(collection);
        buf.setNews(newsService.findById(collection.getNewsId()).orElseThrow());
        return buf;
    }

    @CachePut(value = "messages", key = "#Message.id")
    public Message save(MessageRequestTo Message) {
        MessageRequestTo m = webClient.post().uri("").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Message).exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status == HttpStatus.CREATED) {
                        return response.bodyToMono(MessageRequestTo.class);
                    } else {
                        return Mono.error(new ServiceException("Ошибка", 404));
                    }
                }).block();
        if(m==null)
            throw new ServiceException("Нет такого сообщения", 404);
        Message buf = mapper.mapToBlo(m);
        buf.setNews(newsService.findById(m.getNewsId()).orElseThrow());
        return buf;
    }

    @CachePut(value = "messages", key = "#Message.id")
    public Message update(MessageRequestTo Message) {
        MessageRequestTo m = webClient.put().uri("").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Message).retrieve()
                .bodyToMono(MessageRequestTo.class).block();
        if(m==null)
            throw new ServiceException("Нет такого сообщения", 404);
        Message buf = mapper.mapToBlo(m);
        buf.setNews(newsService.findById(m.getNewsId()).orElseThrow());
        return buf;
    }

    @CacheEvict(value = "messages", key = "#id")
    public void delete(Long id) {
        webClient.delete().uri("/" + id)
                .exchangeToMono(response -> {
                    if (response.statusCode() != HttpStatus.NO_CONTENT)
                        throw new ServiceException("Нечего удалить", 404);
                    return Mono.empty();
                }).block();
    }

    private boolean validator(Message Message) {
        if (Message.getContent().length() < 2 || Message.getContent().length() > 2048
        ) {
            return false;
        }
        return true;
    }
}
