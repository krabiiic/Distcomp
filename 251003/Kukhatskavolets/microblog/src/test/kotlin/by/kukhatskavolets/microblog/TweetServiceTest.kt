package by.kukhatskavolets.microblog

import by.kukhatskavolets.dto.requests.TweetRequestTo
import by.kukhatskavolets.entities.Mark
import by.kukhatskavolets.entities.TweetMark
import by.kukhatskavolets.entities.User
import by.kukhatskavolets.repositories.inMemory.MarkInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.TweetMarkInMemoryRepository
import by.kukhatskavolets.repositories.inMemory.UserInMemoryRepository
import by.kukhatskavolets.services.TweetService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TweetServiceTest {

	@Autowired
	private lateinit var tweetService: TweetService

	@Autowired
	private lateinit var markRepository: MarkInMemoryRepository

	@Autowired
	private lateinit var tweetMarkRepository: TweetMarkInMemoryRepository

	@Autowired
	private lateinit var userRepository: UserInMemoryRepository

	@Test
	fun createTweetTest() {
		val tweetRequestTo = TweetRequestTo(title = "Test Tweet", content = "Test Content", userId = 1)
		val createdTweet = tweetService.createTweet(tweetRequestTo)
		assertNotNull(createdTweet.id)
		assertEquals("Test Tweet", createdTweet.title)
		assertEquals("Test Content", createdTweet.content)
	}

	@Test
	fun getTweetByIdTest() {
		val tweetRequestTo = TweetRequestTo(title = "Find me", content = "Content", userId = 1)
		val createdTweet = tweetService.createTweet(tweetRequestTo)
		val foundTweet = tweetService.getTweetById(createdTweet.id)
		assertEquals(createdTweet.id, foundTweet.id)
		assertEquals("Find me", foundTweet.title)
	}

	@Test
	fun getAllTweetsTest() {
		tweetService.createTweet(TweetRequestTo(title = "Tweet 1", content = "Content 1", userId = 1))
		tweetService.createTweet(TweetRequestTo(title = "Tweet 2", content = "Content 2", userId = 1))
		val tweets = tweetService.getAllTweets()
		assertTrue(tweets.any { it.title == "Tweet 1" })
		assertTrue(tweets.any { it.title == "Tweet 2" })
	}

	@Test
	fun updateTweetTest() {
		val tweetRequestTo = TweetRequestTo(title = "Old Title", content = "Old Content", userId = 1)
		val createdTweet = tweetService.createTweet(tweetRequestTo)
		val updatedTweetRequestTo = TweetRequestTo(title = "New Title", content = "New Content", userId = 1)
		Thread.sleep(10)
		val updatedTweet = tweetService.updateTweet(createdTweet.id, updatedTweetRequestTo)
		assertEquals(createdTweet.id, updatedTweet.id)
		assertEquals("New Title", updatedTweet.title)
		assertEquals("New Content", updatedTweet.content)
		assertTrue(updatedTweet.modified.isAfter(createdTweet.modified))
	}

	@Test
	fun deleteTweetTest() {
		val tweetRequestTo = TweetRequestTo(title = "Title", content = "Content", userId = 1)
		val createdTweet = tweetService.createTweet(tweetRequestTo)
		tweetService.deleteTweet(createdTweet.id)
		assertThrows<NoSuchElementException> {
			tweetService.getTweetById(createdTweet.id)
		}
	}

	@Test
	fun filterTweetsTest() {
		userRepository.save(
			User(
				login = "login",
				password = "",
				firstname = "",
				lastname = ""
			)
		)

		val mark = Mark(id = 1, name = "Important")
		markRepository.save(mark)

		val tweetRequestTo = TweetRequestTo(title = "Important tweet", content = "Detailed content", userId = 1)
		val createdTweet = tweetService.createTweet(tweetRequestTo)
		tweetMarkRepository.save(TweetMark(tweetId = createdTweet.id, markId = mark.id))

		val filteredByMarkName = tweetService.getTweetsByFilters(
			markNames = listOf("importAnt"),
			markIds = null,
			userLogin = null,
			title = null,
			content = null
		)
		assertTrue(filteredByMarkName.any { it.id == createdTweet.id })

		val filteredByMarkId = tweetService.getTweetsByFilters(
			markNames = null,
			markIds = listOf(1),
			userLogin = null,
			title = null,
			content = null
		)
		assertTrue(filteredByMarkId.any { it.id == createdTweet.id })

		val filteredByUser = tweetService.getTweetsByFilters(
			markNames = null,
			markIds = null,
			userLogin = "LoGin",
			title = null,
			content = null
		)
		assertTrue(filteredByUser.any { it.id == createdTweet.id })

		val filteredByTitle = tweetService.getTweetsByFilters(
			markNames = null,
			markIds = null,
			userLogin = null,
			title = "tweEt",
			content = null
		)
		assertTrue(filteredByTitle.any { it.id == createdTweet.id })

		val filteredByContent = tweetService.getTweetsByFilters(
			markNames = null,
			markIds = null,
			userLogin = null,
			title = null,
			content = "DetailEd"
		)
		assertTrue(filteredByContent.any { it.id == createdTweet.id })
	}
}