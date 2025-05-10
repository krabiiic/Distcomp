package by.kukhatskavolets.publisher.repositories

import by.kukhatskavolets.publisher.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLoginIgnoreCase(login: String): User?
}