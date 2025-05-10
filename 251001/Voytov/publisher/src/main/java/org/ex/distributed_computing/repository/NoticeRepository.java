package org.ex.distributed_computing.repository;

import java.util.List;
import org.ex.distributed_computing.model.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {

  List<Notice> findAll();

  @Query(
      value = "SELECT nextval('tbl_notice_id_seq')",
      nativeQuery = true
  )
  Long nextId();
}
