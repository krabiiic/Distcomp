package com.dc.controller;

import com.dc.exception.ServiceException;
import com.dc.mapper.MessageMapper;
import com.dc.model.blo.Message;
import com.dc.model.blo.News;
import com.dc.model.dto.MessageRequestTo;
import com.dc.model.dto.MessageResponseTo;
import com.dc.service.MessageService;
import com.dc.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1.0")
public class MessageController {

    @Autowired
    private MessageMapper mapper;

    @Autowired
    private MessageService service;

    @Autowired
    private NewsService newsService;

    @GetMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<MessageResponseTo>> getAll() {
        Collection<MessageRequestTo> messages = service.getAll();
        List<Message> buf = new ArrayList<>();
        messages.forEach(x->{
            Message buf2 = mapper.mapToBlo(x);
            buf2.setNews(newsService.getById(x.getNewsId()));
            buf.add(buf2);
        });
        return ResponseEntity.ok(List.copyOf(mapper.mapToListDto(buf)));
    }

    @PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponseTo> save(@RequestBody MessageRequestTo MessageRequestTo){
        Message Message = service.save(MessageRequestTo);
        if(Message==null)
        {
            throw new ServiceException("Incorrect user", 404);
        }
        else{
            Message.setNews(newsService.getById(MessageRequestTo.getNewsId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToDto(Message));
        }
    }

    @DeleteMapping(path = "/messages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        service.delete(id);
        HttpStatus status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }

    @GetMapping(path = "/messages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponseTo> get(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(mapper.mapToDto(service.getById(id)));
    }

    @PutMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponseTo> update(@RequestBody MessageRequestTo MessageRequestTo) {
/*        Message buf = mapper.mapToBlo(MessageRequestTo);
        buf.setNews(newsService.getById(MessageRequestTo.getNewsId()));*/
        Message Message = service.update(MessageRequestTo);
        if(Message==null)
        {
            throw new ServiceException("Incorrect user", 404);
        }
        else{
            Message.setNews(newsService.getById(MessageRequestTo.getNewsId()));
            return ResponseEntity.ok(mapper.mapToDto(Message));
        }
    }

}
