package by.kukhatskavolets.dto.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequestTo(
    val id: Long = 0,

    @field:NotBlank(message = "Login may not be blank")
    @field:Size(min = 2, max = 64, message = "Login should be between 2 and 64 symbols")
    val login: String = "",

    @field:NotBlank(message = "Password may not be blank")
    @field:Size(min = 8, max = 128, message = "Password should be between 8 and 128 symbols")
    val password: String = "",

    @field:NotBlank(message = "Firstname may not be blank")
    @field:Size(min = 2, max = 64, message = "Firstname should be between 2 and 64 symbols")
    val firstname: String = "",

    @field:NotBlank(message = "Lastname may not be blank")
    @field:Size(min = 2, max = 64, message = "Lastname should be between 2 and 64 symbols")
    val lastname: String = ""
)