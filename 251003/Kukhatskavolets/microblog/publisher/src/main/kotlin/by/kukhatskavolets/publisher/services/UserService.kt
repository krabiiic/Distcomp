package by.kukhatskavolets.publisher.services

import by.kukhatskavolets.publisher.dto.requests.UserRequestTo
import by.kukhatskavolets.publisher.dto.responses.UserResponseTo
import by.kukhatskavolets.publisher.mappers.toEntity
import by.kukhatskavolets.publisher.mappers.toResponse
import by.kukhatskavolets.publisher.repositories.TweetRepository
import by.kukhatskavolets.publisher.repositories.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tweetRepository: TweetRepository,
) {
    @Caching(
        evict = [
            CacheEvict(value = ["users"], key = "'all_users'")
        ]
    )
    fun createUser(userRequestTo: UserRequestTo): UserResponseTo {
        val user = userRequestTo.toEntity()
        val savedUser = userRepository.save(user)
        return savedUser.toResponse()
    }

    @Cacheable(value = ["users"], key = "#id")
    fun getUserById(id: Long): UserResponseTo {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException() }
        return user.toResponse()
    }

    @Cacheable(value = ["users"], key = "'all_users'")
    fun getAllUsers(): List<UserResponseTo> =
        userRepository.findAll().map { it.toResponse() }

    @Caching(
        put = [CachePut(value = ["users"], key = "#id")],
        evict = [CacheEvict(value = ["users"], key = "'all_users'")]
    )
    fun updateUser(id: Long, userRequestTo: UserRequestTo): UserResponseTo {
        val updatedUser = userRequestTo.toEntity().apply { this.id = id }
        return userRepository.save(updatedUser).toResponse()
    }

    @Caching(
        evict = [
            CacheEvict(value = ["users"], key = "#id"),
            CacheEvict(value = ["users"], key = "'all_users'")
        ]
    )
    fun deleteUser(id: Long) {
        userRepository.findById(id).orElseThrow { NoSuchElementException() }
        userRepository.deleteById(id)
    }

    @Cacheable(value = ["usersByTweet"], key = "#tweetId")
    fun getUserByTweetId(tweetId: Long): UserResponseTo? {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.user.toResponse()
    }
}