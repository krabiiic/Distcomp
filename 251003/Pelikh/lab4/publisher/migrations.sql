CREATE TABLE tbl_creator (
                             id SERIAL PRIMARY KEY,
                             login TEXT NOT NULL UNIQUE CHECK (char_length(login) BETWEEN 2 AND 64),
                             password TEXT NOT NULL CHECK (char_length(password) BETWEEN 8 AND 124),
                             firstname TEXT NOT NULL CHECK (char_length(firstname) BETWEEN 2 AND 64),
                             lastname TEXT NOT NULL CHECK (char_length(lastname) BETWEEN 2 AND 64)
);

CREATE TABLE tbl_topic (
                           id SERIAL PRIMARY KEY,
                           creator_id BIGINT NOT NULL REFERENCES tbl_creator(id),
                           title TEXT NOT NULL UNIQUE CHECK (char_length(title) BETWEEN 2 AND 64),
                           content TEXT NOT NULL CHECK (char_length(content) BETWEEN 4 AND 2048),
                           created TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           modified TIMESTAMPTZ DEFAULT NULL
);

CREATE TABLE tbl_notice (
                            id SERIAL PRIMARY KEY,
                            topic_id BIGINT NOT NULL REFERENCES tbl_topic(id),
                            content TEXT NOT NULL CHECK(char_length(content) BETWEEN 2 AND 2048)
);

CREATE TABLE tbl_marker (
                            id SERIAL PRIMARY KEY,
                            name TEXT UNIQUE CHECK(char_length(name) BETWEEN 2 AND 32) NOT NULL
);

CREATE TABLE tbl_topic_marker (
                                  id SERIAL PRIMARY KEY,
                                  topic_id BIGINT NOT NULL REFERENCES tbl_topic(id),
                                  marker_id BIGINT NOT NULL REFERENCES tbl_marker(id)
);

ALTER TABLE tbl_topic DROP CONSTRAINT IF EXISTS tbl_topic_creator_id_fkey,
                      ADD CONSTRAINT tbl_topic_creator_id_fkey
                          FOREIGN KEY (creator_id) REFERENCES tbl_creator(id)
                              ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS tbl_topic_marker (
                                                topic_id BIGINT NOT NULL,
                                                marker_id BIGINT NOT NULL,
                                                PRIMARY KEY (topic_id, marker_id),
                                                FOREIGN KEY (topic_id) REFERENCES tbl_topic(id) ON DELETE CASCADE,
                                                FOREIGN KEY (marker_id) REFERENCES tbl_marker(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION delete_orphan_markers()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM tbl_marker WHERE id = OLD.marker_id AND NOT EXISTS (
        SELECT 1 FROM tbl_topic_marker
        WHERE marker_id = OLD.marker_id
    );

    RETURN OLD;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER after_delete_topic_marker AFTER DELETE ON tbl_topic_marker
    FOR EACH ROW EXECUTE FUNCTION delete_orphan_markers();