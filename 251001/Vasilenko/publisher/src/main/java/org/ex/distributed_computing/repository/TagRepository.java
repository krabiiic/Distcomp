package org.ex.distributed_computing.repository;

import java.util.List;
import org.ex.distributed_computing.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

  List<Tag> findAll();
}
