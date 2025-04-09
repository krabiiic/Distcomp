package by.kukhatskavolets.repositories.inMemory

import by.kukhatskavolets.entities.TweetMark
import org.springframework.stereotype.Repository

@Repository
class TweetMarkInMemoryRepository: InMemoryRepository<TweetMark>()