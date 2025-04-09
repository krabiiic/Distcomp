package by.kukhatskavolets.repositories.inMemory

import by.kukhatskavolets.entities.Comment
import org.springframework.stereotype.Repository

@Repository
class CommentInMemoryRepository: InMemoryRepository<Comment>()