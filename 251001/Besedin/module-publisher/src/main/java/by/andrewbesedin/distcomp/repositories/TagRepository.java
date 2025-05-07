package by.andrewbesedin.distcomp.repositories;

import by.andrewbesedin.distcomp.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TagRepository extends Repo<Tag> {
    Optional<Tag> findByName(String name);
}
