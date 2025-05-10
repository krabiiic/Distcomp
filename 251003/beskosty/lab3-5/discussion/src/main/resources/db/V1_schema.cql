CREATE KEYSPACE IF NOT EXISTS distcomp WITH REPLICATION  = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };

DROP TABLE IF EXISTS tbl_reaction;

CREATE TABLE tbl_reaction (
                              issue_id BIGINT,
                              id TIMEUUID,
                              content TEXT,
                              state TEXT,
                              PRIMARY KEY (issue_id, id)
);
