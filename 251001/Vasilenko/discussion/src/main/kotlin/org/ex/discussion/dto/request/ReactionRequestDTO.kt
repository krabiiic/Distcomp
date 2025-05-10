package org.ex.discussion.dto.request

data class ReactionRequestDTO(
    val id: Long,
    val country: String?,
    val tweetId: Long,
    val content: String
)
