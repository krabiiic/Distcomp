package by.kukhatskavolets.publisher.repositories

import by.kukhatskavolets.publisher.entities.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long>
