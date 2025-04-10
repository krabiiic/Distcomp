package by.kukhatskavolets.repositories

import by.kukhatskavolets.entities.Mark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MarkRepository : JpaRepository<Mark, Long> {
}
