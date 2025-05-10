package by.kukhatskavolets.discussion.entities

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn

@PrimaryKeyClass
data class CommentKey(
    @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
    val country: String,

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED)
    val id: Long
)