package by.kukhatskavolets.microblog

import by.kukhatskavolets.dto.requests.UserRequestTo
import by.kukhatskavolets.entities.Tweet
import by.kukhatskavolets.repositories.inMemory.TweetInMemoryRepository
import by.kukhatskavolets.services.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tweetRepository: TweetInMemoryRepository

    @Test
    fun createUserTest() {
        val userRequestTo = UserRequestTo(login = "user1", password = "pass", firstname = "First", lastname = "Last")
        val createdUser = userService.createUser(userRequestTo)
        assertNotNull(createdUser.id)
        assertEquals("user1", createdUser.login)
    }

    @Test
    fun getUserByIdTest() {
        val userRequestTo = UserRequestTo(login = "user2", password = "pass", firstname = "First2", lastname = "Last2")
        val createdUser = userService.createUser(userRequestTo)
        val foundUser = userService.getUserById(createdUser.id)
        assertEquals(createdUser.id, foundUser.id)
        assertEquals("user2", foundUser.login)
    }

    @Test
    fun getAllUsersTest() {
        userService.createUser(UserRequestTo(login = "user3", password = "pass", firstname = "First3", lastname = "Last3"))
        userService.createUser(UserRequestTo(login = "user4", password = "pass", firstname = "First4", lastname = "Last4"))
        val users = userService.getAllUsers()
        assertTrue(users.any { it.login == "user3" })
        assertTrue(users.any { it.login == "user4" })
    }

    @Test
    fun updateUserTest() {
        val userRequestTo = UserRequestTo(login = "user5", password = "pass", firstname = "First5", lastname = "Last5")
        val createdUser = userService.createUser(userRequestTo)
        val updatedUserRequestTo = UserRequestTo(login = "updatedUser5", password = "newpass", firstname = "NewFirst5", lastname = "NewLast5")
        val updatedUser = userService.updateUser(createdUser.id, updatedUserRequestTo)
        assertEquals(createdUser.id, updatedUser.id)
        assertEquals("updatedUser5", updatedUser.login)
    }

    @Test
    fun deleteUserTest() {
        val userRequestTo = UserRequestTo(login = "user6", password = "pass", firstname = "First6", lastname = "Last6")
        val createdUser = userService.createUser(userRequestTo)
        userService.deleteUser(createdUser.id)
        assertThrows<NoSuchElementException> {
            userService.getUserById(createdUser.id)
        }
    }

    @Test
    fun getUserByTweetIdTest() {
        val userRequestTo = UserRequestTo(login = "user7", password = "pass", firstname = "First7", lastname = "Last7")
        val createdUser = userService.createUser(userRequestTo)
        val tweet = Tweet(
            id = 1,
            title = "Tweet",
            content = "Content",
            userId = createdUser.id,
        )
        tweetRepository.save(tweet)
        val foundUser = userService.getUserByTweetId(tweet.id)
        assertEquals(createdUser.id, foundUser.id)
        assertEquals("user7", foundUser.login)
    }
}