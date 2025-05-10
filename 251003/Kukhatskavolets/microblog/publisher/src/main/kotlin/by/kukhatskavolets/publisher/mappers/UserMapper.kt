package by.kukhatskavolets.publisher.mappers

import by.kukhatskavolets.publisher.dto.requests.UserRequestTo
import by.kukhatskavolets.publisher.dto.responses.UserResponseTo
import by.kukhatskavolets.publisher.entities.User

fun UserRequestTo.toEntity(): User {
    return User(
        id = this.id,
        login = this.login,
        password = this.password,
        firstname = this.firstname,
        lastname = this.lastname,
    )
}

fun User.toResponse(): UserResponseTo {
    return UserResponseTo(
        id = this.id,
        login = this.login,
        firstname = this.firstname,
        lastname = this.lastname,
    )
}
