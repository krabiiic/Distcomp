package com.rita.publisher.controller;

import com.rita.publisher.dto.*;
import com.rita.publisher.service.StickerService;
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

@Tag(name = "StickerController", description = "Взаимодействие со стикерами")
@RestController
@RequestMapping("/api/v1.0/stickers")
public class StickerController {
    @Autowired
    StickerService stickerService;

    @Operation(summary = "Получить стикер по id",
            description = "Возвращает стикер.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Стикер успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<StickerDTO> getSticker(@PathVariable @Parameter(description = "Идентификатор стикера") Long id){
        return new ResponseEntity<>(stickerService.getSticker(id), HttpStatus.OK);
    }
    @Operation(summary = "Получить все стикеры",
            description = "Возвращает список всех стикеров.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Стикер успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping
    public ResponseEntity<List<StickerDTO>>getStickers() {
        List<StickerDTO> stickers = stickerService.getAllStickers();
        return new ResponseEntity<>(stickers,HttpStatus.OK);
    }

//    @Operation(summary = "Получить все стикеры по tweetId ",
//            description = "Возвращает список всех стикеров по tweetId.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @GetMapping("/tweet/{tweetId:\\d+}")
//    public ResponseEntity<Page<StickerDTO>> getStickersByTweetId(@PageableDefault(size = 10)Pageable pageable,
//            @PathVariable @Parameter(description = "Идентификатор твита") Long tweetId) {
//
//        Page<StickerDTO> page = stickerService.getStickersByTweetId(tweetId, pageable);
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }

    @Operation(summary = "Создать стикер",
            description = "Возвращает созданный StickerDTO.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Стикер успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PostMapping
    public ResponseEntity<StickerDTO> createSticker(@RequestBody @Validated StickerCreateDTO request){
        return new ResponseEntity<> (stickerService.createSticker(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить стикер",
            description = "Возвращает обновлённый StickerDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Стикер успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PutMapping
    public ResponseEntity<StickerDTO> updateSticker(@RequestBody @Validated StickerUpdateDTO request){
        return new ResponseEntity<>(stickerService.updateSticker(request),HttpStatus.OK);
    }

    @Operation(summary = "Удалить стикер по id",
            description = "Удаляет стикер.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Стикер успешно удалён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteSticker(@PathVariable  @Parameter(description = "Идентификатор стикера", required = true,example ="1") Long id){
        stickerService.deleteSticker(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
