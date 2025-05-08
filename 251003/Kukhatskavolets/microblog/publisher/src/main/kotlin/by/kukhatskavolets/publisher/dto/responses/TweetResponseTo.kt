package by.kukhatskavolets.publisher.dto.responses

import java.time.LocalDateTime

data class TweetResponseTo(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val created: LocalDateTime,
    val modified: LocalDateTime
)