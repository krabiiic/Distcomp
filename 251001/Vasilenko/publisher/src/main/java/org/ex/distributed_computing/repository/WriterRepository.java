package org.ex.distributed_computing.repository;

import java.util.List;
import org.ex.distributed_computing.model.Writer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends CrudRepository<Writer, Long> {

  List<Writer> findAll();

  boolean existsByLogin(String login);
}
