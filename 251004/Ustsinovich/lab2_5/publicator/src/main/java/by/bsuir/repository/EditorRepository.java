package by.bsuir.repository;

import by.bsuir.entities.Editor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EditorRepository extends JpaRepository<Editor, Long>, JpaSpecificationExecutor<Editor> {
    boolean existsByLogin(String login);

    @Query("SELECT i.editor FROM Issue i WHERE i.id = :issueId")
    Editor findEditorByIssueId(@Param("issueId") Long issueId);

    Page<Editor> findAll(Pageable pageable);
}
