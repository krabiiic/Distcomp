package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.CommentRequestTo
import by.kukhatskavolets.dto.responses.CommentResponseTo
import by.kukhatskavolets.entities.Comment


fun CommentRequestTo.toEntity(): Comment = Comment(
    id = id,
    tweetId = tweetId,
    content = content
)

fun Comment.toResponse(): CommentResponseTo = CommentResponseTo(
    id = id,
    tweetId = tweetId,
    content = content
)
