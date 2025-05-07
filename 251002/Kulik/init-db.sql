
CREATE TABLE tbl_creator (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    firstname VARCHAR(64) NOT NULL,
    lastname VARCHAR(64) NOT NULL
);

CREATE TABLE tbl_story (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    creator_id BIGINT REFERENCES tbl_creator(id),
    title VARCHAR(64) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger function
CREATE OR REPLACE FUNCTION update_modified_at() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.modified = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Attach the trigger to the `story` table
CREATE TRIGGER trigger_update_modified_at
BEFORE UPDATE ON tbl_story
FOR EACH ROW
EXECUTE FUNCTION update_modified_at();

CREATE TABLE tbl_mark (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    story_id BIGINT REFERENCES tbl_story(id),
    name TEXT NOT NULL
);

CREATE TABLE tbl_note (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    story_id BIGINT NOT NULL REFERENCES tbl_story(id),
    content TEXT NOT NULL
);
