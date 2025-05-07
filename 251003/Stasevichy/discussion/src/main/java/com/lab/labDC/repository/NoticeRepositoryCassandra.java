package com.lab.labDC.repository;

import com.lab.labDC.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepositoryCassandra extends CassandraRepository<Notice, Long> {
    }
