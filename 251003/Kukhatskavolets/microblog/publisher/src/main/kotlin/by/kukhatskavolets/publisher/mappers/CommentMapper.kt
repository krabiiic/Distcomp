package by.kukhatskavolets.publisher.mappers

import by.kukhatskavolets.publisher.dto.requests.CommentRequestTo
import by.kukhatskavolets.publisher.dto.responses.CommentResponseTo
import by.kukhatskavolets.publisher.entities.Comment
import by.kukhatskavolets.publisher.entities.Tweet


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
