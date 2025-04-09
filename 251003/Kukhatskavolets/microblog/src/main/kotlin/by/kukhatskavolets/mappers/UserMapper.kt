package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.UserRequestTo
import by.kukhatskavolets.dto.responses.UserResponseTo
import by.kukhatskavolets.entities.User

fun UserRequestTo.toEntity(): User = User(
    id = id,
    login = this.login,
    password = this.password,
    firstname = this.firstname,
    lastname = this.lastname
)

fun User.toResponse(): UserResponseTo = UserResponseTo(
    id = this.id,
    login = this.login,
    firstname = this.firstname,
    lastname = this.lastname
)
