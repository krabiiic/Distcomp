package by.kukhatskavolets.discussion.mappers

import by.kukhatskavolets.discussion.dto.requests.CommentRequestTo
import by.kukhatskavolets.discussion.dto.responses.CommentResponseTo
import by.kukhatskavolets.discussion.entities.Comment
import by.kukhatskavolets.discussion.entities.CommentKey


fun CommentRequestTo.toEntity(country: String): Comment {
    return Comment(
        CommentKey(country = country, id = id),
        tweetId = tweetId,
        content = content,
    )
}

fun Comment.toResponse(): CommentResponseTo {
    return CommentResponseTo(
        id = key.id,
        tweetId = tweetId,
        content = content,
    )
}
