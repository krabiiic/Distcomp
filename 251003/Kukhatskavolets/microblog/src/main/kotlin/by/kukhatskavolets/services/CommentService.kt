package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.CommentRequestTo
import by.kukhatskavolets.dto.responses.CommentResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.CommentRepository
import by.kukhatskavolets.repositories.TweetRepository
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