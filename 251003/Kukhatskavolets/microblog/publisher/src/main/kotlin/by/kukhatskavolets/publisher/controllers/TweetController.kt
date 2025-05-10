package by.kukhatskavolets.publisher.controllers

import by.kukhatskavolets.publisher.dto.requests.TweetRequestTo
import by.kukhatskavolets.publisher.dto.responses.TweetResponseTo
import by.kukhatskavolets.publisher.services.TweetService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/tweets")
class TweetController(
    private val tweetService: TweetService
) {

    @PostMapping
    fun createTweet(@RequestBody @Valid tweetRequestTo: TweetRequestTo): ResponseEntity<TweetResponseTo> {
        val createdTweet = tweetService.createTweet(tweetRequestTo)
        return ResponseEntity(createdTweet, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getTweetById(@PathVariable id: Long): ResponseEntity<TweetResponseTo> {
        val tweet = tweetService.getTweetById(id)
        return ResponseEntity.ok(tweet)
    }

    @GetMapping
    fun getAllTweets(): ResponseEntity<List<TweetResponseTo>> {
        val tweets = tweetService.getAllTweets()
        return ResponseEntity.ok(tweets)
    }

    @PutMapping()
    fun updateTweet(@RequestBody @Valid tweetRequestTo: TweetRequestTo): ResponseEntity<TweetResponseTo> {
        val updatedTweet = tweetService.updateTweet(tweetRequestTo.id, tweetRequestTo)
        return ResponseEntity.ok(updatedTweet)
    }

    @DeleteMapping("/{id}")
    fun deleteTweet(@PathVariable id: Long): ResponseEntity<Void> {
        tweetService.deleteTweet(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchTweets(
        @RequestParam(required = false) markNames: List<String>?,
        @RequestParam(required = false) markIds: List<Long>?,
        @RequestParam(required = false) userLogin: String?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) content: String?
    ): ResponseEntity<List<TweetResponseTo>> {
        val tweets = tweetService.getTweetsByFilters(markNames, markIds, userLogin, title, content)
        return ResponseEntity.ok(tweets)
    }
}
