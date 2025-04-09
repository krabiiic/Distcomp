package by.kukhatskavolets.microblog

import by.kukhatskavolets.dto.requests.MarkRequestTo
import by.kukhatskavolets.entities.TweetMark
import by.kukhatskavolets.repositories.inMemory.TweetMarkInMemoryRepository
import by.kukhatskavolets.services.MarkService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MarkServiceTest {

    @Autowired
    private lateinit var markService: MarkService

    @Autowired
    private lateinit var tweetMarkRepository: TweetMarkInMemoryRepository

    @Test
    fun createMarkTest() {
        val markRequestTo = MarkRequestTo(name = "Test Mark")
        val createdMark = markService.createMark(markRequestTo)
        assertNotNull(createdMark.id)
        assertEquals("Test Mark", createdMark.name)
    }

    @Test
    fun getMarkByIdTest() {
        val markRequestTo = MarkRequestTo(name = "Mark 1")
        val createdMark = markService.createMark(markRequestTo)
        val foundMark = markService.getMarkById(createdMark.id)
        assertEquals(createdMark.id, foundMark.id)
        assertEquals("Mark 1", foundMark.name)
    }

    @Test
    fun getAllMarksTest() {
        markService.createMark(MarkRequestTo(name = "Mark 1"))
        markService.createMark(MarkRequestTo(name = "Mark 2"))
        val marks = markService.getAllMarks()
        assertTrue(marks.any { it.name == "Mark 1" })
        assertTrue(marks.any { it.name == "Mark 2" })
    }

    @Test
    fun updateMarkTest() {
        val markRequestTo = MarkRequestTo(name = "Old Mark")
        val createdMark = markService.createMark(markRequestTo)
        val updatedMarkRequestTo = MarkRequestTo(name = "New Mark")
        val updatedMark = markService.updateMark(createdMark.id, updatedMarkRequestTo)
        assertEquals(createdMark.id, updatedMark.id)
        assertEquals("New Mark", updatedMark.name)
    }

    @Test
    fun deleteMarkTest() {
        val markRequestTo = MarkRequestTo(name = "To Delete")
        val createdMark = markService.createMark(markRequestTo)
        markService.deleteMark(createdMark.id)
        assertThrows<NoSuchElementException> {
            markService.getMarkById(createdMark.id)
        }
    }

    @Test
    fun getMarksByTweetIdTest() {
        val markRequestTo = MarkRequestTo(name = "Important")
        val createdMark = markService.createMark(markRequestTo)
        tweetMarkRepository.save(TweetMark(tweetId = 1, markId = createdMark.id))
        val marks = markService.getMarksByTweetId(1)
        assertTrue(marks.any { it.id == createdMark.id })
    }
}