package by.bsuir.repository;

import by.bsuir.entities.Note;
import by.bsuir.entities.NoteKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CassandraRepository<Note, NoteKey> {
    List<Note> findByIssueId(Long issueId);
    List<Note> findById (Long id);
    void deleteByCountryAndIssueIdAndId (String country, Long issueId, Long id);
    int countByCountry (String country);
}
