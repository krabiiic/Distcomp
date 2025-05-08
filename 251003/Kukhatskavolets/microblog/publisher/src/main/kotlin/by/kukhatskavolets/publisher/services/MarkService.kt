package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.MarkRequestTo
import by.kukhatskavolets.publisher.dto.responses.MarkResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.MarkRepository
import by.kukhatskavolets.publisher.repositories.TweetRepository
import org.springframework.stereotype.Service

@Service
class MarkService(
    private val markRepository: MarkRepository,
    private val tweetRepository: TweetRepository,
) {
    fun createMark(markRequestTo: MarkRequestTo): MarkResponseTo {
        val mark = markRequestTo.toEntity()
        val savedMark = markRepository.save(mark)
        return savedMark.toResponse()
    }

    fun getMarkById(id: Long): MarkResponseTo {
        val mark = markRepository.findById(id).orElseThrow { NoSuchElementException() }
        return mark.toResponse()
    }

    fun getAllMarks(): List<MarkResponseTo> =
        markRepository.findAll().map { it.toResponse() }

    fun updateMark(id: Long, markRequestTo: MarkRequestTo): MarkResponseTo {
        val updatedMark = markRequestTo.toEntity().apply { this.id = id }
        return markRepository.save(updatedMark).toResponse()
    }

    fun deleteMark(id: Long) {
        markRepository.findById(id).orElseThrow { NoSuchElementException() }
        markRepository.deleteById(id)
    }

    fun getMarksByTweetId(tweetId: Long): List<MarkResponseTo> {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.marks.map { it.toResponse() }
    }
}