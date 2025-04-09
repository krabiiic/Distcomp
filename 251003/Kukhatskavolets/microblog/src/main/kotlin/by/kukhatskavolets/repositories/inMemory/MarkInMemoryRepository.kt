package by.kukhatskavolets.repositories.inMemory

import by.kukhatskavolets.entities.Mark
import org.springframework.stereotype.Repository

@Repository
class MarkInMemoryRepository: InMemoryRepository<Mark>()