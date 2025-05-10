USE distcomp;
CREATE TABLE IF NOT EXISTS id_counter
(
    name text PRIMARY KEY,
    value counter
);