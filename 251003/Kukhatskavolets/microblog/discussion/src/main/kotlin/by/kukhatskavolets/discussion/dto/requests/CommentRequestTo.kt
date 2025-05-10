package by.kukhatskavolets.discussion.dto.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CommentRequestTo(
    val id: Long = 0,

    @field:NotNull(message = "Tweet id can't be null")
    val tweetId: Long = 0,

    @field:NotBlank(message = "Content may not be blank")
    @field:Size(min = 2, max = 2048, message = "Content should be between 2 and 2048 symbols")
    val content: String = ""
)