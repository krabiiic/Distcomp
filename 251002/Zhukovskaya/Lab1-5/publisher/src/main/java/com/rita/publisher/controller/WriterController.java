package com.rita.publisher.controller;

import com.rita.publisher.dto.*;
import com.rita.publisher.service.WriterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "WriterController", description = "Взаимодействие с writer")
@RestController
@RequestMapping("/api/v1.0/writers")
public class WriterController {
    @Autowired
    WriterService writerService;

    @Operation(summary = "Получить writer по id",
            description = "Возвращает writer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Writer успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<WriterDTO> getWriter(@PathVariable Long id){
        System.out.println("getWriter id:"+id);
        WriterDTO writerDTO=writerService.getWriter(id);
        System.out.println("getWriter writerDTO:"+writerDTO);
        return new ResponseEntity<>(writerDTO,HttpStatus.OK);
    }


    @Operation(summary = "Получить все writers",
            description = "Возвращает список всех writers.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping
    public ResponseEntity<List<WriterDTO>>getWriters() {
        List<WriterDTO> writers = writerService.getAllWriters();
        return new ResponseEntity<>(writers,HttpStatus.OK);
    }

//    @Operation(summary = "Получить writer по TweetId",
//            description = "Возвращает writer по TweetId.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Writer успешно возвращён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @GetMapping("/tweet/{tweetId:\\d+}")
//    public ResponseEntity<WriterDTO> getWriterByTweetId(@PathVariable Long tweetId){
//        WriterDTO writerDTO=writerService.getWriterByTweetId(tweetId);
//        return new ResponseEntity<>(writerDTO,HttpStatus.OK);
//    }

    @Operation(summary = "Обновить writer",
            description = "Возвращает обновлённый WriterDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Writer успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PutMapping
    public ResponseEntity<WriterDTO> updateWriter(@RequestBody @Validated(UpdateGroup.class) WriterUpdateDTO request){
        System.out.println("updateWriter request"+request);
        return new ResponseEntity<>(writerService.updateWriter(request),HttpStatus.OK);
    }

//    @Operation(summary = "Обновить writer по некторым полям",
//            description = "Возвращает обновлённый WriterDTO.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Writer успешно обновлён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @PatchMapping
//    public ResponseEntity<WriterDTO> patchWriter(@RequestBody  @Validated WriterUpdateDTO request){
//        return new ResponseEntity<>(writerService.patchWriter(request),HttpStatus.CREATED);
//    }
    @Operation(summary = "Создать writer",
            description = "Возвращает созданный WriterDTO.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Writer успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PostMapping
    public ResponseEntity<WriterDTO> createWriter(@RequestBody @Validated WriterCreateDTO request){
        return new ResponseEntity<>(writerService.createWriter(request),HttpStatus.CREATED);
    }

    @Operation(summary = "Удалить writer по id",
            description = "Удаляет writer.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Writer успешно удалён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteWriter(@PathVariable Long id){
        writerService.deleteWriter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
