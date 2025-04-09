package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.TweetMarkRequestTo
import by.kukhatskavolets.dto.responses.TweetMarkResponseTo
import by.kukhatskavolets.entities.TweetMark

fun TweetMarkRequestTo.toEntity(): TweetMark = TweetMark(
    tweetId = tweetId,
    markId = markId,
)

fun TweetMark.toResponse(): TweetMarkResponseTo = TweetMarkResponseTo(
    tweetId = tweetId,
    markId = markId,
)
