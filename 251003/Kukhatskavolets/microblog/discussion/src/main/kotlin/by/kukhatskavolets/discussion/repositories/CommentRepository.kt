package by.kukhatskavolets.discussion.repositories

import by.kukhatskavolets.discussion.entities.Comment
import by.kukhatskavolets.discussion.entities.CommentKey
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : CassandraRepository<Comment, CommentKey>
