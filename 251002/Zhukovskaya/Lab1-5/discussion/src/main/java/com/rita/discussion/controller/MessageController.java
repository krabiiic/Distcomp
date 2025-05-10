package com.rita.discussion.controller;

import com.rita.discussion.dto.*;
import com.rita.discussion.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MessageController", description = "Взаимодействие с сообщениями")
@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Operation(summary = "Получить сообщение по id",
            description = "Возвращает сообщение.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сообщение успешно возвращено"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable @Parameter(description = "Идентификатор сообщения") Long id){
        return new ResponseEntity<>(messageService.getMessage(id), HttpStatus.OK);
    }

    @Operation(summary = "Получить все сообщения",
            description = "Возвращает список всех сообщений.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages(){
        List<MessageDTO> list=messageService.getAllMessages();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Operation(summary = "Создать сообщение",
            description = "Возвращает созданный MessageDTO.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Сообщение успешно создано"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody @Validated MessageCreateDTO request){
        return new ResponseEntity<> (messageService.createMessage(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить сообщение",
            description = "Возвращает обновлённый MessageDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сообщение успешно обновлёно"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PutMapping
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody @Validated(UpdateGroup.class) MessageUpdateDTO request){
        return new ResponseEntity<>(messageService.updateMessage(request),HttpStatus.OK);
    }

    @Operation(summary = "Удалить сообщение по id",
            description = "Удаляет сообщение.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Сообщение успешно удалено"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteMessage(@PathVariable  @Parameter(description = "Идентификатор сообщения", required = true,example ="1") Long id){
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/ex")
    public ResponseEntity<MessageDTO> createMessageExchange(@RequestBody @Validated MessageCreateDTO request){
        return new ResponseEntity<> (messageService.createMessage(request), HttpStatus.CREATED);
    }
    @PutMapping("/ex")
    public ResponseEntity<MessageDTO> updateMessageExchange(@RequestBody @Validated(UpdateGroup.class) MessageUpdateDTO request){
        return new ResponseEntity<>(messageService.updateMessage(request),HttpStatus.OK);
    }

}
