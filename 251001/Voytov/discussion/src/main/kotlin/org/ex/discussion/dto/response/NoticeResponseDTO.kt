package org.ex.discussion.dto.response

data class NoticeResponseDTO (
    val id: Long,
    val country: String,
    val newsId: Long,
    val content: String
)