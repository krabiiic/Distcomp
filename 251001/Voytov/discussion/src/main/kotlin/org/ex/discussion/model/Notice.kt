package org.ex.discussion.model

import lombok.Getter
import lombok.Setter
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Setter
@Getter
@Table("notice")
data class Notice (

    @PrimaryKey
    @Column("id")
    val id: Long,

    @Column("country")
    val country: String,

    @Column("newsId")
    var newsId: Long,

    @Column("content")
    var content: String
)