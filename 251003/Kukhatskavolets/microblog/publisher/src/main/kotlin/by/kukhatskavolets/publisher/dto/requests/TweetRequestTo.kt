package by.kukhatskavolets.publisher.dto.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class TweetRequestTo(
    val id: Long = 0,

    @field:NotNull(message = "User id can't be null")
    val userId: Long = 0,

    val marks: List<String> = listOf(),

    @field:NotBlank(message = "Title may not be blank")
    @field:Size(min = 2, max = 64, message = "Title should be between 2 and 64 symbols")
    val title: String = "",

    @field:NotBlank(message = "Content may not be blank")
    @field:Size(min = 4, max = 2048, message = "Content should be between 4 and 2048 symbols")
    val content: String = ""
)