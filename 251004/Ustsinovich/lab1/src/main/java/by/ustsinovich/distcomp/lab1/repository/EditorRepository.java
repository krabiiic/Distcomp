package by.ustsinovich.distcomp.lab1.repository;

import by.ustsinovich.distcomp.lab1.model.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
}
