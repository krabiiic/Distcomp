package by.kukhatskavolets.repositories.inMemory

import by.kukhatskavolets.entities.User
import org.springframework.stereotype.Repository


@Repository
class UserInMemoryRepository: InMemoryRepository<User>()