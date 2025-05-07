package by.andrewbesedin.distcomp.repositories;

import by.andrewbesedin.distcomp.entities.Reaction;
import by.andrewbesedin.distcomp.entities.ReactionKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction, ReactionKey> {
    default Stream<Reaction> getAll(){
        return StreamSupport.stream(findAll().spliterator(), false);
    }
    default Optional<Reaction> get(ReactionKey id){
        return findById(id);
    }
    default Optional<Reaction> create(Reaction input){
        return Optional.of(save(input));
    }
    default Optional<Reaction> update(Reaction input){
        return Optional.of(save(input));
    }
    default void delete(ReactionKey id){
        if(findById(id).isPresent()) {
            deleteById(id);
        } else {
            throw new NoSuchElementException("No element with id " + id);
        }
    }
}
