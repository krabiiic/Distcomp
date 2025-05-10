package by.kukhatskavolets.discussion.controllers

import by.kukhatskavolets.discussion.dto.requests.CommentRequestTo
import by.kukhatskavolets.discussion.dto.responses.CommentResponseTo
import by.kukhatskavolets.discussion.services.CommentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun createComment(
        @RequestBody @Valid commentRequestTo: CommentRequestTo,
        @RequestHeader("X-Country", defaultValue = "Belarus") country: String
    ): ResponseEntity<CommentResponseTo> {
        val createdComment = commentService.createComment(commentRequestTo, country)
        println(createdComment)
        return ResponseEntity(createdComment, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllComments(): ResponseEntity<List<CommentResponseTo>> {
        val comments = commentService.getAllComments()
        return ResponseEntity.ok(comments)
    }

    @GetMapping("/{id}")
    fun getCommentById(
        @PathVariable id: Long,
        @RequestHeader("X-Country", defaultValue = "Belarus") country: String
    ): ResponseEntity<CommentResponseTo> {
        val comment = commentService.getCommentById(country, id)
        return ResponseEntity.ok(comment)
    }

    @DeleteMapping("/{id}")
    fun deleteComment(
        @PathVariable id: Long,
        @RequestHeader("X-Country", defaultValue = "Belarus") country: String
    ): ResponseEntity<Void> {
        commentService.deleteComment(country, id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping()
    fun updateComment(
        @RequestBody @Valid commentRequestTo: CommentRequestTo,
        @RequestHeader("X-Country", defaultValue = "Belarus") country: String
    ): ResponseEntity<CommentResponseTo> {
        val updatedComment = commentService.updateComment(country, commentRequestTo)
        return ResponseEntity.ok(updatedComment)
    }
}