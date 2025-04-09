package by.kukhatskavolets.entities

data class TweetMark(
    override var id: Long = 0L,
    val tweetId: Long,
    val markId: Long
) : Identifiable