package by.ustsinovich.distcomp.lab1.repository;

import by.ustsinovich.distcomp.lab1.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
}
