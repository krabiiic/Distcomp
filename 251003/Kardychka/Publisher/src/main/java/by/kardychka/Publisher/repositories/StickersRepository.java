package by.kardychka.Publisher.repositories;

import by.kardychka.Publisher.models.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StickersRepository extends JpaRepository<Sticker, Long> {
    boolean existsByName(String name);
    List<Sticker> findByNameIn(Set<String> names);
}
