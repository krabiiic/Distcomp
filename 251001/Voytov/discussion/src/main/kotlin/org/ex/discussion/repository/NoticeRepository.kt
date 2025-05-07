package org.ex.discussion.repository

import org.ex.discussion.model.Notice
import org.springframework.data.cassandra.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NoticeRepository : CrudRepository<Notice, Long> {

    @Query(
        value = "SELECT * FROM notice WHERE id = :id ALLOW FILTERING;",
        allowFiltering = true
    )
    override fun findById(@Param("id") id: Long): Optional<Notice>

    @Query(
        value = "DELETE FROM notice WHERE id = :id AND country = :country;"
    )
    fun delete(@Param("id") id: Long, @Param("country") country: String)
}