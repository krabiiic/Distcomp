package by.kukhatskavolets.dto.requests

import jakarta.validation.constraints.Size

data class CommentRequestTo(
    val id: Long = 0,
    val tweetId: Long = 0,

    @field:Size(min = 2, max = 2048)
    val content: String = ""
)