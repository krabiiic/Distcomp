package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.CommentRequestTo
import by.kukhatskavolets.publisher.dto.responses.CommentResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.CommentRepository
import by.kukhatskavolets.publisher.repositories.TweetRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val tweetRepository: TweetRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    @Caching(
        evict = [
            CacheEvict(value = ["comments"], key = "'all_comments'"),
            CacheEvict(value = ["commentsByTweet"], key = "#commentRequestTo.tweetId")
        ]
    )
    fun createComment(commentRequestTo: CommentRequestTo): CommentResponseTo {
        val tweet = tweetRepository.findById(commentRequestTo.tweetId).orElseThrow { NoSuchElementException() }
        val comment = commentRequestTo.toEntity(tweet)
        val savedComment = commentRepository.save(comment)
        val commentResponse = savedComment.toResponse()
        sendCommentCreatedEvent(commentResponse)
        return commentResponse
    }

    @Cacheable(value = ["comments"], key = "#id")
    fun getCommentById(id: Long): CommentResponseTo {
        val comment = commentRepository.findById(id).orElseThrow { NoSuchElementException() }
        return comment.toResponse()
    }

    @Cacheable(value = ["comments"], key = "'all_comments'")
    fun getAllComments(): List<CommentResponseTo> =
        commentRepository.findAll().map { it.toResponse() }

    @Caching(
        put = [CachePut(value = ["comments"], key = "#id")],
        evict = [
            CacheEvict(value = ["comments"], key = "'all_comments'"),
            CacheEvict(value = ["commentsByTweet"], key = "#commentRequestTo.tweetId")
        ]
    )
    fun updateComment(id: Long, commentRequestTo: CommentRequestTo): CommentResponseTo {
        val tweet = tweetRepository.findById(commentRequestTo.tweetId).orElseThrow { NoSuchElementException() }
        val updatedComment = commentRequestTo.toEntity(tweet).apply { this.id = id }
        val commentResponse = commentRepository.save(updatedComment).toResponse()
        sendCommentUpdatedEvent(commentResponse)
        return commentResponse
    }

    @Caching(
        evict = [
            CacheEvict(value = ["comments"], key = "#id"),
            CacheEvict(value = ["comments"], key = "'all_comments'"),
            CacheEvict(value = ["commentsByTweet"], allEntries = true)
        ]
    )
    fun deleteComment(id: Long) {
        commentRepository.findById(id).orElseThrow { NoSuchElementException() }
        commentRepository.deleteById(id)
        sendCommentDeletedEvent(id)
    }

    @Cacheable(value = ["commentsByTweet"], key = "#tweetId")
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