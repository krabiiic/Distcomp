package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.MarkRequestTo
import by.kukhatskavolets.publisher.dto.responses.MarkResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.MarkRepository
import by.kukhatskavolets.publisher.repositories.TweetRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class MarkService(
    private val markRepository: MarkRepository,
    private val tweetRepository: TweetRepository,
) {
    @Caching(
        evict = [
            CacheEvict(value = ["marks"], key = "'all_marks'")
        ]
    )
    fun createMark(markRequestTo: MarkRequestTo): MarkResponseTo {
        val mark = markRequestTo.toEntity()
        val savedMark = markRepository.save(mark)
        return savedMark.toResponse()
    }

    @Cacheable(value = ["marks"], key = "#id")
    fun getMarkById(id: Long): MarkResponseTo {
        val mark = markRepository.findById(id).orElseThrow { NoSuchElementException() }
        return mark.toResponse()
    }

    @Cacheable(value = ["marks"], key = "'all_marks'")
    fun getAllMarks(): List<MarkResponseTo> =
        markRepository.findAll().map { it.toResponse() }

    @Caching(
        put = [CachePut(value = ["marks"], key = "#id")],
        evict = [CacheEvict(value = ["marks"], key = "'all_marks'")]
    )
    fun updateMark(id: Long, markRequestTo: MarkRequestTo): MarkResponseTo {
        val updatedMark = markRequestTo.toEntity().apply { this.id = id }
        return markRepository.save(updatedMark).toResponse()
    }

    @Caching(
        evict = [
            CacheEvict(value = ["marks"], key = "#id"),
            CacheEvict(value = ["marks"], key = "'all_marks'")
        ]
    )
    fun deleteMark(id: Long) {
        markRepository.findById(id).orElseThrow { NoSuchElementException() }
        markRepository.deleteById(id)
    }

    @Cacheable(value = ["marksByTweets"], key = "#tweetId")
    fun getMarksByTweetId(tweetId: Long): List<MarkResponseTo> {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.marks.map { it.toResponse() }
    }
}