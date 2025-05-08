package by.kukhatskavolets.publisher.dto.responses

data class CommentResponseTo(
    val id: Long,
    val tweetId: Long,
    val content: String
)