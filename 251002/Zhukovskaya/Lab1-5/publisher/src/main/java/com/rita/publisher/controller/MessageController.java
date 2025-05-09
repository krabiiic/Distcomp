package com.rita.publisher.controller;

import com.rita.publisher.dto.*;
import com.rita.publisher.kafka.MessageService;
import com.rita.publisher.repository.TweetRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Tag(name = "MessageController", description = "Взаимодействие с сообщениями")
@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    TweetRepository tweetRepository;
    @Operation(summary = "Получить сообщение по id",
            description = "Возвращает сообщение.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сообщение успешно возвращено"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable @Parameter(description = "Идентификатор сообщения") Long id) throws ExecutionException, InterruptedException, TimeoutException {
        return messageService.getMessage(id);
    }

    @Operation(summary = "Получить все сообщения",
            description = "Возвращает список всех сообщений.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages() throws ExecutionException, InterruptedException, TimeoutException {
        return messageService.getAllMessages();
    }

    @Operation(summary = "Создать сообщение",
            description = "Возвращает созданный MessageDTO.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Сообщение успешно создано"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody @Validated MessageCreateDTO request) {
       if (tweetRepository.existsById(request.tweetId())){
           return messageService.createMessage(request);
       }
       else throw new EntityNotFoundException("Tweet not found with id: " + request.tweetId());

    }

    @Operation(summary = "Обновить сообщение",
            description = "Возвращает обновлённый MessageDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сообщение успешно обновлёно"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PutMapping
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody @Validated(UpdateGroup.class) MessageUpdateDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        if (tweetRepository.existsById(request.tweetId())){
            return messageService.updateMessage(request);
        }
            else throw new EntityNotFoundException("Tweet not found with id: " + request.tweetId());
    }

    @Operation(summary = "Удалить сообщение по id",
            description = "Удаляет сообщение.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Сообщение успешно удалено"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteMessage(@PathVariable  @Parameter(description = "Идентификатор сообщения", required = true,example ="1") Long id) throws ExecutionException, InterruptedException, TimeoutException {
       return messageService.deleteMessage(id);
    }
}
