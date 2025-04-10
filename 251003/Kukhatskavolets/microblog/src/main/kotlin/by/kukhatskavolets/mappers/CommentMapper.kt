package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.CommentRequestTo
import by.kukhatskavolets.dto.responses.CommentResponseTo
import by.kukhatskavolets.entities.Comment
import by.kukhatskavolets.entities.Tweet


fun CommentRequestTo.toEntity(tweet: Tweet): Comment {
    return Comment(
        id = this.id,
        tweet = tweet,
        content = this.content,
    )
}

fun Comment.toResponse(): CommentResponseTo {
    return CommentResponseTo(
        id = this.id,
        tweetId = this.tweet.id,
        content = this.content,
    )
}
