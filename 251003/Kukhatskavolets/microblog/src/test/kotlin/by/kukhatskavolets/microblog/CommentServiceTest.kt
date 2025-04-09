package by.kukhatskavolets.microblog

import by.kukhatskavolets.dto.requests.CommentRequestTo
import by.kukhatskavolets.services.CommentService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private lateinit var commentService: CommentService

    @Test
    fun createCommentTest() {
        val commentRequestTo = CommentRequestTo(content = "Content", tweetId = 1)
        val createdComment = commentService.createComment(commentRequestTo)
        assertNotNull(createdComment.id)
        assertEquals("Content", createdComment.content)
        assertEquals(1, createdComment.tweetId)
    }

    @Test
    fun getCommentByIdTest() {
        val commentRequestTo = CommentRequestTo(content = "Content", tweetId = 2)
        val createdComment = commentService.createComment(commentRequestTo)
        val foundComment = commentService.getCommentById(createdComment.id)
        assertEquals(createdComment.id, foundComment.id)
    }

    @Test
    fun getAllCommentsTest() {
        commentService.createComment(CommentRequestTo(content = "Content 1", tweetId = 1))
        commentService.createComment(CommentRequestTo(content = "Content 2", tweetId = 1))
        val comments = commentService.getAllComments()
        assertTrue(comments.any { it.content == "Content 1" })
        assertTrue(comments.any { it.content == "Content 2" })
    }

    @Test
    fun updateCommentTest() {
        val commentRequestTo = CommentRequestTo(content = "Old Content", tweetId = 1)
        val createdComment = commentService.createComment(commentRequestTo)
        val updatedCommentRequestTo = CommentRequestTo(content = "New Content", tweetId = 1)
        val updatedComment = commentService.updateComment(createdComment.id, updatedCommentRequestTo)
        assertEquals(createdComment.id, updatedComment.id)
        assertEquals("New Content", updatedComment.content)
    }

    @Test
    fun deleteCommentTest() {
        val commentRequestTo = CommentRequestTo(content = "Content", tweetId = 1)
        val createdComment = commentService.createComment(commentRequestTo)
        commentService.deleteComment(createdComment.id)
        assertThrows<NoSuchElementException> {
            commentService.getCommentById(createdComment.id)
        }
    }

    @Test
    fun getCommentsByTweetIdTest() {
        val comment1 = commentService.createComment(CommentRequestTo(content = "Content 1", tweetId = 10))
        val comment2 = commentService.createComment(CommentRequestTo(content = "Content 2", tweetId = 10))
        commentService.createComment(CommentRequestTo(content = "Content 3", tweetId = 20))
        val comments = commentService.getCommentsByTweetId(10)
        assertTrue(comments.any { it.id == comment1.id })
        assertTrue(comments.any { it.id == comment2.id })
        assertFalse(comments.any { it.tweetId == 20L })
    }
}