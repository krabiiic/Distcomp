//package com.example.publisher.controller;
//
//import com.example.publisher.dto.ReactionRequestTo;
//import com.example.publisher.dto.ReactionResponseTo;
//import com.example.publisher.service.ReactionService;
//import com.example.publisher.service.ReactionTransportService;
//import com.example.publisher.state.ReactionState;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1.0/reactions")
//@RequiredArgsConstructor
//public class ReactionController {
//
//    private final ReactionTransportService transportService;
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public ReactionResponseTo createReaction(@RequestBody @Valid ReactionRequestTo reactionRequestTo) {
//        reactionRequestTo = new ReactionRequestTo(
//                reactionRequestTo.country(),
//                generateId(), // Генерируй ID, если он нужен до отправки
//                reactionRequestTo.topicId(),
//                reactionRequestTo.content(),
//                reactionRequestTo.state()
//        );
//        return transportService.sendReaction(reactionRequestTo);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/{id}")
//    public ReactionResponseTo getReaction(@PathVariable Long id) {
//        ReactionRequestTo request = new ReactionRequestTo(null, id, null, null, ReactionState.PENDING);
//        return transportService.sendReaction(request);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{id}")
//    public void deleteReaction(@PathVariable Long id) {
//        ReactionRequestTo request = new ReactionRequestTo(null, id, null, null, ReactionState.DECLINE);
//        transportService.sendReaction(request);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @PutMapping
//    public ReactionResponseTo updateReaction(@RequestBody @Valid ReactionRequestTo reactionRequestTo) {
//        return transportService.sendReaction(reactionRequestTo);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping
//    public List<ReactionResponseTo> getAllReactions() {
//        // Для получения всех реакций — отправь "заглушку" с id = -1 или по какому-то флагу
//        ReactionRequestTo request = new ReactionRequestTo(null, -1L, null, null, ReactionState.PENDING);
//        ReactionResponseTo response = transportService.sendReaction(request);
//        // Предположим, что response.content() содержит JSON со списком
//        // Здесь лучше сразу менять транспорт под возврат списка
//        return List.of(response); // Заглушка — подправим, если нужно получить реальный список
//    }
//
//    // Генерация ID — временно, пока не получаешь его снаружи
//    private long generateId() {
//        return System.currentTimeMillis();
//    }
//}
//
package com.example.publisher.controller;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import com.example.publisher.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReactionResponseTo createReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.createReaction(reactionRequestTo).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ReactionResponseTo getReaction(@PathVariable Long id) {
        return reactionService.getReactionById(id).block();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReactionResponseTo> getAllReactions() {
        return reactionService.getAllReactions()
                .collectList()
                .block();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReactionResponseTo updateReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.updateReaction(reactionRequestTo);
    }
}

