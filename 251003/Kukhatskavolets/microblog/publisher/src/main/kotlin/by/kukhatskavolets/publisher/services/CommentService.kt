package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.CommentRequestTo
import by.kukhatskavolets.publisher.dto.responses.CommentResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.CommentRepository
import by.kukhatskavolets.publisher.repositories.TweetRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val tweetRepository: TweetRepository,
) {
    fun createComment(commentRequestTo: CommentRequestTo): CommentResponseTo {
        val tweet = tweetRepository.findById(commentRequestTo.tweetId).orElseThrow { NoSuchElementException() }
        val comment = commentRequestTo.toEntity(tweet)
        val savedComment = commentRepository.save(comment)
        return savedComment.toResponse()
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
        return commentRepository.save(updatedComment).toResponse()
    }

    fun deleteComment(id: Long) {
        commentRepository.findById(id).orElseThrow { NoSuchElementException() }
        commentRepository.deleteById(id)
    }

    fun getCommentsByTweetId(tweetId: Long): List<CommentResponseTo> {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.comments.map { it.toResponse() }
    }
}