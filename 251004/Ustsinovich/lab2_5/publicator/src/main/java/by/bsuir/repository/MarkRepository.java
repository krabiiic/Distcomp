package by.bsuir.repository;

import by.bsuir.entities.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long>, JpaSpecificationExecutor<Mark> {
    @Query("SELECT i.marks FROM Issue i WHERE i.id = :issueId")
    List<Mark> findMarksByIssueId(@Param("issueId") Long issueId);

    Page<Mark> findAll(Pageable pageable);
}
