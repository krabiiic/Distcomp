package com.rita.publisher.controller;

import com.rita.publisher.dto.*;
import com.rita.publisher.service.TweetService;
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

@Tag(name = "TweetController", description = "Взаимодействие с tweets")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {
    @Autowired
    private TweetService tweetService;


    @Operation(summary = "Получить tweet по id",
            description = "Возвращает tweet.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Твит успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<TweetDTO> getTweet(@PathVariable @Parameter(description = "Идентификатор твита", required = true,example ="1") Long id){
        TweetDTO tweetDTO=tweetService.getTweet(id);
        return new ResponseEntity<>(tweetDTO,HttpStatus.OK);
    }

//    @Operation(summary = "Получить все tweets по критерям поиска",
//            description = "Возвращает список всех tweets по критерям поиска.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @GetMapping("/search")
//    public ResponseEntity<Page<TweetFullDTO>> searchTweets(@PageableDefault(size = 10) Pageable pageable,@RequestParam(required = false) Set<Long> stickerIds,
//                                                    @RequestParam(required = false) Set<String> stickerNames,
//                                                    @RequestParam(required = false) String writerLogin,
//                                                    @RequestParam(required = false) String title,
//                                                    @RequestParam(required = false) String content){
//       Page<TweetFullDTO> tweetDTO=tweetService.searchTweets(pageable,stickerIds, stickerNames, writerLogin, title, content);
//        return new ResponseEntity<>(tweetDTO,HttpStatus.OK);
//    }

//    @Operation(summary = "Добавить стикер к твиту",
//            description = "Возвращает TweetDTO..",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @GetMapping("/addSticker")
//    public ResponseEntity<TweetFullDTO> addStickerToTweet(@RequestParam Long tweetId,
//                                                          @RequestParam Long stickerId){
//        TweetFullDTO tweetDTO=tweetService.addSticker(tweetId,stickerId);
//        return new ResponseEntity<>(tweetDTO,HttpStatus.OK);
//    }
//    @Operation(summary = "Удалить стикер к твиту",
//            description = "Возвращает TweetDTO..",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @DeleteMapping("/deleteSticker")
//    public ResponseEntity<TweetFullDTO> deleteStickerFromTweet(@RequestParam Long tweetId,
//                                                          @RequestParam Long stickerId){
//        TweetFullDTO tweetDTO=tweetService.deleteSticker(tweetId,stickerId);
//        return new ResponseEntity<>(tweetDTO,HttpStatus.OK);
//    }

    @Operation(summary = "Получить все tweets",
            description = "Возвращает список всех tweets.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно возвращён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @GetMapping
    public ResponseEntity<List<TweetDTO>> getAllTweets(){
        List<TweetDTO> list=tweetService.getAllTweets();

        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @Operation(summary = "Создать tweet",
            description = "Возвращает созданный TweetDTO.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Твит успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PostMapping
    public ResponseEntity<TweetDTO> createTweet(@RequestBody @Validated TweetCreateDTO request){
        return new ResponseEntity<> (tweetService.createTweet(request), HttpStatus.CREATED);
    }
    @Operation(summary = "Обновить tweet",
            description = "Возвращает обновлённый TweetDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Твит успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @PutMapping
    public ResponseEntity<TweetDTO> updateTweet(@RequestBody @Validated(UpdateGroup.class) TweetUpdateDTO request){
        return new ResponseEntity<>(tweetService.updateTweet(request),HttpStatus.OK);
    }

//    @Operation(summary = "Обновить tweet по некторым полям",
//            description = "Возвращает обновлённый TweetDTO.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Твит успешно обновлён"),
//                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
//            })
//    @PatchMapping
//    public ResponseEntity<TweetDTO> patchTweet(@RequestBody @Validated TweetUpdateDTO request){
//        return new ResponseEntity<>(tweetService.patchTweet(request),HttpStatus.OK);
//    }

    @Operation(summary = "Удалить tweet по id",
            description = "Удаляет твит.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Твит успешно удалён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            })
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteTweet(@PathVariable  @Parameter(description = "Идентификатор твита", required = true,example ="1") Long id){
        tweetService.deleteTweet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
