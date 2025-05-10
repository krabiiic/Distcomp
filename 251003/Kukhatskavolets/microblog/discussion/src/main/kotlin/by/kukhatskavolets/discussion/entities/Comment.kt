package by.kukhatskavolets.discussion.entities

import jakarta.validation.constraints.Size
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("tbl_comment")
data class Comment(
    @PrimaryKey
    var key: CommentKey,

    val tweetId: Long,

    @field:Size(min = 2, max = 2048)
    val content: String  = ""
)