CREATE KEYSPACE IF NOT EXISTS distcomp WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE distcomp;
CREATE TABLE IF NOT EXISTS tbl_reaction (
    id int PRIMARY KEY,
    content text,
    issueId int
);