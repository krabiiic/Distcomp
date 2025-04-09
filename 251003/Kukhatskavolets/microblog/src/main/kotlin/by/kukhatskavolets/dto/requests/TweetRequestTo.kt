package by.kukhatskavolets.dto.requests

import jakarta.validation.constraints.Size

data class TweetRequestTo(
    val id: Long = 0,
    val userId: Long = 0,

    @field:Size(min = 2, max = 64)
    val title: String = "",

    @field:Size(min = 4, max = 2048)
    val content: String = ""
)