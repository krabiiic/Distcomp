package by.kukhatskavolets.publisher.repositories

import by.kukhatskavolets.publisher.entities.Tweet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TweetRepository : JpaRepository<Tweet, Long>, JpaSpecificationExecutor<Tweet> {
    @Query(
        """
        SELECT DISTINCT t 
        FROM Tweet t 
        JOIN t.marks m 
        WHERE m.id IN :markIds 
        GROUP BY t 
        HAVING COUNT(DISTINCT m.id) = :markCount
        """
    )
    fun findByAllMarkIds(@Param("markIds") markIds: List<Long>, @Param("markCount") markCount: Long): List<Tweet>

    @Query(
        """
        SELECT DISTINCT t 
        FROM Tweet t 
        JOIN t.marks m 
        WHERE m.name IN :markNames 
        GROUP BY t 
        HAVING COUNT(DISTINCT m.name) = :markCount
        """
    )
    fun findByAllMarkNames(@Param("markNames") markNames: List<String>, @Param("markCount") markCount: Long): List<Tweet>
}