package com.dc.service;

import com.dc.exception.ServiceException;
import com.dc.model.blo.Message;
import com.dc.model.dto.MessageRequestTo;
import com.dc.model.dto.NewsDTO;
import com.dc.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class MessageService{

    private WebClient webClient = WebClient.create("http://localhost:24110/api/v1.0/news");

    @Autowired
    private MessageRepository repository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public Collection<Message> getAll() {
        return repository.findAll();
    }

    public Message getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ServiceException("Сообщение не найдено", 404));
    }

    public Message save(Message Message) {
        Message.setId(sequenceGeneratorService.generateSequence("messageId"));
        if(!validator(Message))
        {
            throw new ServiceException("Не корректный", 404);
        }
        return repository.save(Message);
    }

    public Message update(Message Message) {
        Message buf = repository.findById(Message.getId()).orElseThrow(() -> new ServiceException("Не существует", 404));
        if(!validator(Message))
        {
            throw new ServiceException("Не корректный", 404);
        }
        return repository.save(Message);
    }

    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new ServiceException("Нечего удалить", 404));
        repository.deleteById(id);
    }

    private boolean validator(Message Message){
        NewsDTO n = webClient.get().uri("/"+Message.getNews()).exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status == HttpStatus.OK) {
                        return response.bodyToMono(NewsDTO.class);
                    } else {
                        return Mono.error(new ServiceException("Ошибка", 404));
                    }
                }).block();
        if (Message.getContent().length() < 2 || Message.getContent().length() > 2048
        ) {
            return false;
        }
        return true;
    }
}
