package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.TweetRequestTo
import by.kukhatskavolets.dto.responses.TweetResponseTo
import by.kukhatskavolets.entities.Mark
import by.kukhatskavolets.entities.Tweet
import by.kukhatskavolets.entities.User
import java.time.LocalDateTime


fun TweetRequestTo.toEntity(user: User, marks: MutableSet<Mark>) = Tweet(
    id = this.id,
    user = user,
    title = this.title,
    content = this.content,
    marks = marks,
    created = LocalDateTime.now(),
    modified = LocalDateTime.now()
)

fun Tweet.toResponse() = TweetResponseTo(
    id = this.id,
    userId = this.user.id,
    title = this.title,
    content = this.content,
    created = this.created,
    modified = this.modified
)