package by.kukhatskavolets.discussion.dto.responses

data class CommentResponseTo(
    val id: Long = 0,
    val tweetId: Long = 0,
    val content: String = ""
)