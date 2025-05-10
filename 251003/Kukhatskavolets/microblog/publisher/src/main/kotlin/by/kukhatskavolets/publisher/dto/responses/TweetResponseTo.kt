package by.kukhatskavolets.publisher.dto.responses

import java.time.LocalDateTime

data class TweetResponseTo(
    val id: Long = 0,
    val userId: Long = 0,
    val title: String = "",
    val content: String = "",
    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime = LocalDateTime.now(),
)