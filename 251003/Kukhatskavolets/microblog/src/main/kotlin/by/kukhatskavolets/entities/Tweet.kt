package by.kukhatskavolets.entities

import java.time.LocalDateTime

data class Tweet(
    override var id: Long = 0L,
    val userId: Long,
    val title: String,
    val content: String,
    val created: LocalDateTime = LocalDateTime.now(),
    var modified: LocalDateTime = LocalDateTime.now(),
) : Identifiable