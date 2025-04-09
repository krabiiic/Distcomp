package by.kukhatskavolets.services

import by.kukhatskavolets.dto.requests.UserRequestTo
import by.kukhatskavolets.dto.responses.UserResponseTo
import by.kukhatskavolets.mappers.toEntity
import by.kukhatskavolets.mappers.toResponse
import by.kukhatskavolets.repositories.inMemory.TweetInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.UserInMemoryRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserInMemoryRepository,
    private val tweetRepository: TweetInMemoryRepository,
) {
    fun createUser(userRequestTo: UserRequestTo): UserResponseTo {
        val user = userRequestTo.toEntity()
        val savedUser = userRepository.save(user)
        return savedUser.toResponse()
    }

    fun getUserById(id: Long): UserResponseTo {
        val user = userRepository.findById(id)
        return user.toResponse()
    }

    fun getAllUsers(): List<UserResponseTo> =
        userRepository.findAll().map { it.toResponse() }

    fun updateUser(id: Long, userRequestTo: UserRequestTo): UserResponseTo {
        val updatedUser = userRequestTo.toEntity().apply { this.id = id }
        return userRepository.update(updatedUser).toResponse()
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    fun getUserByTweetId(tweetId: Long): UserResponseTo {
        val tweet = tweetRepository.findById(tweetId)
        val user = userRepository.findById(tweet.userId)
        return user.toResponse()
    }
}