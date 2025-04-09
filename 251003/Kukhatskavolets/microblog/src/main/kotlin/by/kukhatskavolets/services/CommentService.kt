package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.CommentRequestTo
import by.kukhatskavolets.dto.responses.CommentResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.inMemory.CommentInMemoryRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentInMemoryRepository,
) {
    fun createComment(commentRequestTo: CommentRequestTo): CommentResponseTo {
        val comment = commentRequestTo.toEntity()
        val savedComment = commentRepository.save(comment)
        return savedComment.toResponse()
    }

    fun getCommentById(id: Long): CommentResponseTo {
        val comment = commentRepository.findById(id)
        return comment.toResponse()
    }

    fun getAllComments(): List<CommentResponseTo> =
        commentRepository.findAll().map { it.toResponse() }

    fun updateComment(id: Long, commentRequestTo: CommentRequestTo): CommentResponseTo {
        val updatedComment = commentRequestTo.toEntity().apply { this.id = id }
        return commentRepository.update(updatedComment).toResponse()
    }

    fun deleteComment(id: Long) {
        commentRepository.deleteById(id)
    }

    fun getCommentsByTweetId(issueId: Long): List<CommentResponseTo> {
        return commentRepository.findAll()
            .filter { it.tweetId == issueId }
            .map { it.toResponse() }
    }
}