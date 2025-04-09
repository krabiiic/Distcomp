package by.kukhatskavolets.repositories.inMemory

import by.kukhatskavolets.entities.Tweet
import org.springframework.stereotype.Repository

@Repository
class TweetInMemoryRepository: InMemoryRepository<Tweet>()