package by.kukhatskavolets.publisher.repositories

import by.kukhatskavolets.publisher.entities.Mark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MarkRepository : JpaRepository<Mark, Long>
