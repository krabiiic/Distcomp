package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.TweetRequestTo
import by.kukhatskavolets.dto.responses.TweetResponseTo
import by.kukhatskavolets.entities.Mark
import by.kukhatskavolets.entities.Tweet
import by.kukhatskavolets.entities.User
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.TweetRepository
import by.kukhatskavolets.repositories.UserRepository
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val userRepository: UserRepository,
) {
    fun createTweet(tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val user = userRepository.findById(tweetRequestTo.userId).orElseThrow { NoSuchElementException() }
        val marks = tweetRequestTo.marks.map { Mark(name = it) }.toMutableSet()
        val tweet = tweetRequestTo.toEntity(user, marks)
        val savedTweet = tweetRepository.save(tweet)
        return savedTweet.toResponse()
    }

    fun getTweetById(id: Long): TweetResponseTo {
        val tweet = tweetRepository.findById(id).orElseThrow { NoSuchElementException() }
        return tweet.toResponse()
    }

    fun getAllTweets(): List<TweetResponseTo> =
        tweetRepository.findAll().map { it.toResponse() }

    fun updateTweet(id: Long, tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val user = userRepository.findById(tweetRequestTo.userId).orElseThrow { NoSuchElementException() }
        val marks = tweetRequestTo.marks.map { Mark(name = it) }.toMutableSet()
        val updatedTweet = tweetRequestTo.toEntity(user, marks).apply {
            this.id = id
            this.modified = LocalDateTime.now()
        }
        return tweetRepository.save(updatedTweet).toResponse()
    }

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