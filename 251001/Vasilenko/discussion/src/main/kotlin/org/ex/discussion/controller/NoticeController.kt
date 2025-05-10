package org.ex.discussion.controller

import org.ex.discussion.dto.request.ReactionRequestDTO
import org.ex.discussion.dto.response.ReactionResponseDTO
import org.ex.discussion.service.ReactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reactions")
class ReactionController(val reactionService: ReactionService) {

    @GetMapping
    fun getAllReactions(): List<ReactionResponseDTO> = reactionService.getAllReactions()

    @GetMapping("/{id}")
    fun getReactionById(@PathVariable id: Long): ReactionResponseDTO = reactionService.getReactionById(id)

    @PostMapping
    fun createReaction(@RequestBody reactionRequest: ReactionRequestDTO): ResponseEntity<ReactionResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(reactionService.createReaction(reactionRequest))
    }

    @PutMapping
    fun updateReaction(@RequestBody reactionRequest: ReactionRequestDTO): ResponseEntity<ReactionResponseDTO> {
        println("Received updating reaction request $reactionRequest")
        return ResponseEntity.ok(reactionService.updateReaction(reactionRequest))
    }

    @DeleteMapping("/{id}")
    fun deleteReaction(@PathVariable("id") reactionId: Long): ResponseEntity<Void> {
        reactionService.deleteReaction(reactionId)
        return ResponseEntity.ok().build()
    }
}