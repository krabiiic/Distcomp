package by.kapinskiy.Discussion.repositories;


import by.kapinskiy.Discussion.models.Note;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotesRepository extends CrudRepository<Note, Note.NoteKey> {
    @Query("SELECT * FROM tbl_note WHERE country = :country AND id = :id")
    Optional<Note> findByCountryAndId(@Param("country") String country, @Param("id") Long id);

    @Query("DELETE FROM tbl_note WHERE country = :country AND id = :id")
    void deleteByCountryAndId(@Param("country") String country, @Param("id") Long id);
}
