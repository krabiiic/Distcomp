package by.kukhatskavolets.discussion.services

import by.kukhatskavolets.discussion.dto.requests.CommentRequestTo
import by.kukhatskavolets.discussion.dto.responses.CommentResponseTo
import by.kukhatskavolets.discussion.entities.CommentKey
import by.kukhatskavolets.discussion.mappers.toEntity
import by.kukhatskavolets.discussion.mappers.toResponse
import by.kukhatskavolets.discussion.repositories.CommentRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentRepository: CommentRepository) {
    fun createComment(commentRequestTo: CommentRequestTo, country: String): CommentResponseTo =
        commentRepository.save(commentRequestTo.toEntity(country)).toResponse()

    fun getCommentById(country: String, id: Long): CommentResponseTo {
        val commentKey = CommentKey(country, id)
        return commentRepository.findById(commentKey).orElseThrow {
            NoSuchElementException()
        }.toResponse()
    }

    fun getAllComments(): List<CommentResponseTo> =
        commentRepository.findAll().map { it.toResponse() }

    fun updateComment(country: String, commentRequestTo: CommentRequestTo): CommentResponseTo {
        val commentKey = CommentKey(country, commentRequestTo.id)
        val existingComment = commentRepository.findById(commentKey).orElseThrow {
            NoSuchElementException()
        }
        val updatedComment = existingComment.copy(
            tweetId = commentRequestTo.tweetId,
            content = commentRequestTo.content
        )
        return commentRepository.save(updatedComment).toResponse()
    }

    fun deleteComment(country: String, id: Long) {
        val commentKey = CommentKey(country, id)
        commentRepository.findById(commentKey).orElseThrow {
            NoSuchElementException()
        }
        commentRepository.deleteById(commentKey)
    }

    @KafkaListener(topics = ["comment-create"], groupId = "discussion-group")
    fun listenToCommentCreate(commentRequestTo: CommentRequestTo) {
        createComment(commentRequestTo, "Belarus")
    }

    @KafkaListener(topics = ["comment-update"], groupId = "discussion-group")
    fun listenToCommentUpdate(commentRequestTo: CommentRequestTo) {
        updateComment("Belarus", commentRequestTo)
    }

    @KafkaListener(topics = ["comment-delete"], groupId = "discussion-group")
    fun listenToCommentDelete(id: Long) {
        deleteComment("Belarus", id)
    }

}