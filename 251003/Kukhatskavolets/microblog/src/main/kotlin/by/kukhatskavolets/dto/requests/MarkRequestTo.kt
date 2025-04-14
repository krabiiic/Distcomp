package by.kukhatskavolets.dto.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MarkRequestTo(
    val id: Long = 0,

    @field:NotBlank(message = "Name may not be blank")
    @field:Size(min = 2, max = 32, message = "Name should be between 2 and 32 symbols")
    val name: String = ""
)