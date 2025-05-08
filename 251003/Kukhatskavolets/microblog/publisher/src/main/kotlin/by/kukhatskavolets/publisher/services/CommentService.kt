package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.CommentRequestTo
import by.kukhatskavolets.publisher.dto.responses.CommentResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.CommentRepository
import by.kukhatskavolets.publisher.repositories.TweetRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val tweetRepository: TweetRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun createComment(commentRequestTo: CommentRequestTo): CommentResponseTo {
        val tweet = tweetRepository.findById(commentRequestTo.tweetId).orElseThrow { NoSuchElementException() }
        val comment = commentRequestTo.toEntity(tweet)
        val savedComment = commentRepository.save(comment)
        val commentResponse = savedComment.toResponse()
        sendCommentCreatedEvent(commentResponse)
        return commentResponse
    }

    fun getCommentById(id: Long): CommentResponseTo {
        val comment = commentRepository.findById(id).orElseThrow { NoSuchElementException() }
        return comment.toResponse()
    }

    fun getAllComments(): List<CommentResponseTo> =
        commentRepository.findAll().map { it.toResponse() }

    fun updateComment(id: Long, commentRequestTo: CommentRequestTo): CommentResponseTo {
        val tweet = tweetRepository.findById(commentRequestTo.tweetId).orElseThrow { NoSuchElementException() }
        val updatedComment = commentRequestTo.toEntity(tweet).apply { this.id = id }
        val commentResponse = commentRepository.save(updatedComment).toResponse()
        sendCommentUpdatedEvent(commentResponse)
        return commentResponse
    }

    fun deleteComment(id: Long) {
        commentRepository.findById(id).orElseThrow { NoSuchElementException() }
        commentRepository.deleteById(id)
        sendCommentDeletedEvent(id)
    }

    fun getCommentsByTweetId(tweetId: Long): List<CommentResponseTo> {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.comments.map { it.toResponse() }
    }

    private fun sendCommentCreatedEvent(post: CommentResponseTo) {
        kafkaTemplate.send("comment-create", post)
    }

    private fun sendCommentUpdatedEvent(post: CommentResponseTo) {
        kafkaTemplate.send("comment-update", post)
    }

    private fun sendCommentDeletedEvent(postId: Long) {
        kafkaTemplate.send("comment-delete", postId)
    }
}