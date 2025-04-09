package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.TweetRequestTo
import by.kukhatskavolets.dto.responses.TweetResponseTo
import by.kukhatskavolets.entities.Tweet


fun TweetRequestTo.toEntity(): Tweet = Tweet(
    id = id,
    userId = userId,
    title = title,
    content = content,
)

fun Tweet.toResponse(): TweetResponseTo = TweetResponseTo(
    id = id,
    userId = userId,
    title = title,
    content = content,
    created = created,
    modified = modified
)