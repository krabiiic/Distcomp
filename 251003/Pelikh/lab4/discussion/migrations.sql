CREATE KEYSPACE IF NOT EXISTS distcomp WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1};

USE distcomp;

CREATE TABLE IF NOT EXISTS tbl_notice (
                                          id bigint,
                                          topic_id bigint,
                                          content text,
                                          PRIMARY KEY (id)
);