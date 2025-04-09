package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.TweetRequestTo
import by.kukhatskavolets.dto.responses.TweetResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.inMemory.MarkInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.TweetInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.TweetMarkInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.UserInMemoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TweetService(
    private val tweetRepository: TweetInMemoryRepository,
    private val markRepository: MarkInMemoryRepository,
    private val tweetMarkRepository: TweetMarkInMemoryRepository,
    private val userRepository: UserInMemoryRepository,
) {
    fun createTweet(tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val tweet = tweetRequestTo.toEntity()
        val savedTweet = tweetRepository.save(tweet)
        return savedTweet.toResponse()
    }

    fun getTweetById(id: Long): TweetResponseTo {
        val tweet = tweetRepository.findById(id)
        return tweet.toResponse()
    }

    fun getAllTweets(): List<TweetResponseTo> =
        tweetRepository.findAll().map { it.toResponse() }

    fun updateTweet(id: Long, tweetRequestTo: TweetRequestTo): TweetResponseTo {
        val updatedTweet = tweetRequestTo.toEntity().apply {
            this.id = id
            this.modified = LocalDateTime.now()
        }
        return tweetRepository.update(updatedTweet).toResponse()
    }

    fun deleteTweet(id: Long) {
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

        val markIdsByNames = markRepository.findAll()
            .filter { mark -> markNames.any { it.contains(mark.name, ignoreCase = true) } }
            .map { it.id }
            .toSet()

        return tweetMarkRepository.findAll()
            .filter { it.markId in markIdsByNames }
            .map { it.tweetId }
            .toSet()
    }

    private fun getTweetIdsByMarkIds(markIds: List<Long>?): Set<Long>? {
        if (markIds.isNullOrEmpty()) return null

        return tweetMarkRepository.findAll()
            .filter { it.markId in markIds }
            .map { it.tweetId }
            .toSet()
    }

    private fun getUserIdByLogin(userLogin: String?): Long? {
        if (userLogin.isNullOrBlank()) return null

        val userId = userRepository.findAll()
            .firstOrNull { it.login.equals(userLogin, ignoreCase = true) }
            ?.id
        return userId ?: -1
    }

    private fun filterTweets(
        tweetIdsByNames: Set<Long>?,
        tweetIdsByMarkIds: Set<Long>?,
        userId: Long?,
        title: String?,
        content: String?
    ): List<TweetResponseTo> {
        return tweetRepository.findAll()
            .filter { tweet ->
                (tweetIdsByNames == null || tweet.id in tweetIdsByNames) &&
                (tweetIdsByMarkIds == null || tweet.id in tweetIdsByMarkIds) &&
                (userId == null || tweet.userId == userId) &&
                (title == null || tweet.title.contains(title, ignoreCase = true)) &&
                (content == null || tweet.content.contains(content, ignoreCase = true))
            }
            .map { it.toResponse() }
    }
}