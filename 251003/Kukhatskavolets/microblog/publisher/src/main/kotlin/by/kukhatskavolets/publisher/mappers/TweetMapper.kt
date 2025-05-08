package by.kukhatskavolets.publisher.mappers

import by.kukhatskavolets.publisher.dto.requests.TweetRequestTo
import by.kukhatskavolets.publisher.dto.responses.TweetResponseTo
import by.kukhatskavolets.publisher.entities.Mark
import by.kukhatskavolets.publisher.entities.Tweet
import by.kukhatskavolets.publisher.entities.User
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