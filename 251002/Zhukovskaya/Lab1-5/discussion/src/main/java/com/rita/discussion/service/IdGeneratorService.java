package com.rita.discussion.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {

    @Autowired
    private CassandraOperations cassandraOps;

    public Long getNextId() {
        cassandraOps.getCqlOperations().execute("UPDATE id_counter SET value = value + 1 WHERE name = 'message_id'");

        Long nextId = cassandraOps.selectOne("SELECT value FROM id_counter WHERE name = 'message_id'", Long.class);
        return nextId;
    }
}