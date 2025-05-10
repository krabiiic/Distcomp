package org.ex.distributed_computing.repository;

import java.util.List;
import org.ex.distributed_computing.model.Reaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction, Long> {

  List<Reaction> findAll();

  @Query(
      value = "SELECT nextval('tbl_reaction_id_seq')",
      nativeQuery = true
  )
  Long nextId();
}
