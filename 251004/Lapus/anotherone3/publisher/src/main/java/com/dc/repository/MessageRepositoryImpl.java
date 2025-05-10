package com.dc.repository;

import com.dc.mapper.MessageMapper;
import com.dc.model.blo.Message;
import com.dc.model.dto.MessageRequestTo;
import com.dc.service.MessageService;
import com.dc.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

public class MessageRepositoryImpl implements MessageRepository{

    private WebClient webClient = WebClient.create("http://localhost:24130/api/v1.0/messages");

    @Autowired
    private MessageMapper mapper;

    @Autowired
    private NewsRepository newsService;

    public Collection<MessageRequestTo> findAll() {
        Collection<MessageRequestTo> collection = webClient.get().uri("").retrieve().bodyToFlux(MessageRequestTo.class).collectList().block();
        return collection;
    }

    public Message findById(Long id) {
        MessageRequestTo collection = webClient.get().uri("/"+id).retrieve().bodyToMono(MessageRequestTo.class).block();

        Message buf = mapper.mapToBlo(collection);
        buf.setNews(newsService.findById(collection.getNewsId()).orElseThrow());
        return buf;
    }



}
