package by.kukhatskavolets.dto.requests

import jakarta.validation.constraints.Size

data class MarkRequestTo(
    val id: Long = 0,

    @field:Size(min = 2, max = 32)
    val name: String = ""
)