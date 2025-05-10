package by.ustsinovich.distcomp.lab1.repository;

import by.ustsinovich.distcomp.lab1.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
