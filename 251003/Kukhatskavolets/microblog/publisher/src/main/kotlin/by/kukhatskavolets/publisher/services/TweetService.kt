package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.TweetRequestTo
import by.kukhatskavolets.publisher.dto.responses.TweetResponseTo
import by.kukhatskavolets.publisher.entities.Mark
import by.kukhatskavolets.publisher.entities.Tweet
import by.kukhatskavolets.publisher.entities.User
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.TweetRepository
import by.kukhatskavolets.publisher.repositories.UserRepository
import jakarta.persistence.criteria.Predicate
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val userRepository: UserRepository,
) {
    @Caching(
        evict = [
            CacheEvict(value = ["tweets"], key = "'all_tweets'"),
        ]
    )
    fun createTweet(tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val user = userRepository.findById(tweetRequestTo.userId).orElseThrow { NoSuchElementException() }
        val marks = tweetRequestTo.marks.map { Mark(name = it) }.toMutableSet()
        val tweet = tweetRequestTo.toEntity(user, marks)
        val savedTweet = tweetRepository.save(tweet)
        return savedTweet.toResponse()
    }

    @Cacheable(value = ["tweets"], key = "#id")
    fun getTweetById(id: Long): TweetResponseTo {
        val tweet = tweetRepository.findById(id).orElseThrow { NoSuchElementException() }
        return tweet.toResponse()
    }

    @Cacheable(value = ["tweets"], key = "'all_tweets'")
    fun getAllTweets(): List<TweetResponseTo> =
        tweetRepository.findAll().map { it.toResponse() }

    @Caching(
        put = [CachePut(value = ["tweets"], key = "#id")],
        evict = [CacheEvict(value = ["tweets"], key = "'all_tweets'")]
    )
    fun updateTweet(id: Long, tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val user = userRepository.findById(tweetRequestTo.userId).orElseThrow { NoSuchElementException() }
        val marks = tweetRequestTo.marks.map { Mark(name = it) }.toMutableSet()
        val updatedTweet = tweetRequestTo.toEntity(user, marks).apply {
            this.id = id
            this.modified = LocalDateTime.now()
        }
        return tweetRepository.save(updatedTweet).toResponse()
    }

    @Caching(
        evict = [
            CacheEvict(value = ["tweets"], key = "#id"),
            CacheEvict(value = ["tweets"], key = "'all_tweets'")
        ]
    )
    fun deleteTweet(id: Long) {
        tweetRepository.findById(id).orElseThrow { NoSuchElementException() }
        tweetRepository.deleteById(id)
    }

    fun getTweetsByFilters(
        markNames: List<String>?,
        markIds: List<Long>?,
        userLogin: String?,
        title: String?,
        content: String?
    ): List<TweetResponseTo> {
        val tweetIdsByNames = getTweetIdsByMarkNames(markNames)
        val tweetIdsByMarkIds = getTweetIdsByMarkIds(markIds)
        val userId = getUserIdByLogin(userLogin)

        return filterTweets(tweetIdsByNames, tweetIdsByMarkIds, userId, title, content)
    }

    private fun getTweetIdsByMarkNames(markNames: List<String>?): Set<Long>? {
        if (markNames.isNullOrEmpty()) return null

        val lowerCaseNames = markNames.map { it.lowercase() }
        return tweetRepository
            .findByAllMarkNames(lowerCaseNames, lowerCaseNames.count().toLong())
            .map { it.id }.toSet()
    }

    private fun getTweetIdsByMarkIds(markIds: List<Long>?): Set<Long>? {
        if (markIds.isNullOrEmpty()) return null

        return tweetRepository
            .findByAllMarkIds(markIds, markIds.count().toLong())
            .map { it.id }.toSet()
    }

    private fun getUserIdByLogin(userLogin: String?): Long? {
        if (userLogin.isNullOrBlank()) return null

        val user = userRepository.findByLoginIgnoreCase(userLogin)
        return user?.id ?: -1
    }

    private fun filterTweets(
        tweetIdsByNames: Set<Long>?,
        tweetIdsByMarkIds: Set<Long>?,
        userId: Long?,
        title: String?,
        content: String?
    ): List<TweetResponseTo> {
        val specification =
            TweetSpecifications.filterByParams(tweetIdsByNames, tweetIdsByMarkIds, userId, title, content)
        return tweetRepository.findAll(specification).map { it.toResponse() }
    }
}

object TweetSpecifications {
    fun filterByParams(
        tweetIdsByNames: Set<Long>?,
        tweetIdsByMarkIds: Set<Long>?,
        userId: Long?,
        title: String?,
        content: String?
    ): Specification<Tweet> {
        return Specification { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            tweetIdsByNames?.let {
                predicates.add(root.get<Long>("id").`in`(it))
            }

            tweetIdsByMarkIds?.let {
                predicates.add(root.get<Long>("id").`in`(it))
            }

            userId?.let {
                predicates.add(criteriaBuilder.equal(root.get<User>("user").get<Long>("id"), it))
            }

            title?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%${it.lowercase()}%"))
            }

            content?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%${it.lowercase()}%"))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}