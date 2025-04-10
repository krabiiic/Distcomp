package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.UserRequestTo
import by.kukhatskavolets.dto.responses.UserResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.TweetRepository
import by.kukhatskavolets.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tweetRepository: TweetRepository,
) {
    fun createUser(userRequestTo: UserRequestTo): UserResponseTo {
        val user = userRequestTo.toEntity()
        val savedUser = userRepository.save(user)
        return savedUser.toResponse()
    }

    fun getUserById(id: Long): UserResponseTo {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException() }
        return user.toResponse()
    }

    fun getAllUsers(): List<UserResponseTo> =
        userRepository.findAll().map { it.toResponse() }

    fun updateUser(id: Long, userRequestTo: UserRequestTo): UserResponseTo {
        val updatedUser = userRequestTo.toEntity().apply { this.id = id }
        return userRepository.save(updatedUser).toResponse()
    }

    fun deleteUser(id: Long) {
        userRepository.findById(id).orElseThrow { NoSuchElementException() }
        userRepository.deleteById(id)
    }

    fun getUserByTweetId(tweetId: Long): UserResponseTo? {
        val tweet = tweetRepository.findById(tweetId).orElseThrow { NoSuchElementException() }
        return tweet.user.toResponse()
    }
}