package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.MarkRequestTo
import by.kukhatskavolets.dto.responses.MarkResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.inMemory.MarkInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.TweetMarkInMemoryRepository
import org.springframework.stereotype.Service

@Service
class MarkService(
    private val markRepository: MarkInMemoryRepository,
    private val tweetMarkRepository: TweetMarkInMemoryRepository,
) {
    fun createMark(markRequestTo: MarkRequestTo): MarkResponseTo {
        val mark = markRequestTo.toEntity()
        val savedMark = markRepository.save(mark)
        return savedMark.toResponse()
    }

    fun getMarkById(id: Long): MarkResponseTo {
        val mark = markRepository.findById(id)
        return mark.toResponse()
    }

    fun getAllMarks(): List<MarkResponseTo> =
        markRepository.findAll().map { it.toResponse() }

    fun updateMark(id: Long, markRequestTo: MarkRequestTo): MarkResponseTo {
        val updatedMark = markRequestTo.toEntity().apply { this.id = id }
        return markRepository.update(updatedMark).toResponse()
    }

    fun deleteMark(id: Long) {
        markRepository.deleteById(id)
    }

    fun getMarksByTweetId(tweetId: Long): List<MarkResponseTo> {
        val markLabels = tweetMarkRepository.findAll().filter { it.tweetId == tweetId }
        return markLabels.map { getMarkById(it.markId) }
    }
}