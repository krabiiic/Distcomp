package by.kukhatskavolets.entities

data class Comment(
    override var id: Long = 0L,
    val tweetId: Long,
    val content: String
) : Identifiable