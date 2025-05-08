package by.kukhatskavolets.publisher.controllers

import by.kukhatskavolets.publisher.dto.requests.CommentRequestTo
import by.kukhatskavolets.publisher.dto.responses.CommentResponseTo
import by.kukhatskavolets.publisher.services.CommentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/api/v1.0/comments")
class CommentController(
    private val commentService: CommentService,
    private val webClient: WebClient
) {

    @PostMapping
    fun createComment(@RequestBody @Valid commentRequestTo: CommentRequestTo): ResponseEntity<CommentResponseTo> {
        val createdComment = commentService.createComment(commentRequestTo)
        sendPostRequest(createdComment)
        return ResponseEntity(createdComment, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getCommentById(@PathVariable id: Long): ResponseEntity<CommentResponseTo> {
        val comment = commentService.getCommentById(id)
        return ResponseEntity.ok(comment)
    }

    @GetMapping
    fun getAllComments(): ResponseEntity<List<CommentResponseTo>> {
        val comments = commentService.getAllComments()
        return ResponseEntity.ok(comments)
    }

    @PutMapping()
    fun updateComment(@RequestBody @Valid commentRequestTo: CommentRequestTo): ResponseEntity<CommentResponseTo> {
        val updatedComment = commentService.updateComment(commentRequestTo.id, commentRequestTo)
        sendPutRequest(updatedComment)
        return ResponseEntity.ok(updatedComment)
    }

    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable id: Long): ResponseEntity<Void> {
        sendDeleteRequest(id)
        commentService.deleteComment(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-tweet/{tweetId}")
    fun getCommentsByTweetId(@PathVariable tweetId: Long): ResponseEntity<List<CommentResponseTo>> {
        val comments = commentService.getCommentsByTweetId(tweetId)
        return ResponseEntity.ok(comments)
    }

    private val url = "http://localhost:24130/api/v1.0/comments"

    fun sendPostRequest(body: CommentResponseTo) {
        webClient.post()
            .uri(url)
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .subscribe()
    }

    fun sendPutRequest(body: CommentResponseTo) {
        webClient.put()
            .uri(url)
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .subscribe()
    }

    fun sendDeleteRequest(id: Long) {
        webClient.delete()
            .uri("$url/$id")
            .retrieve()
            .toBodilessEntity()
            .subscribe()
    }
}
