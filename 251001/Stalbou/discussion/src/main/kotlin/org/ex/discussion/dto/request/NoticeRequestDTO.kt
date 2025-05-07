package org.ex.discussion.dto.request

data class NoticeRequestDTO(
    val id: Long,
    val country: String?,
    val newsId: Long,
    val content: String
)
