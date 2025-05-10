DROP TABLE IF EXISTS tbl_issue_mark CASCADE;
DROP TABLE IF EXISTS tbl_issue CASCADE;
DROP TABLE IF EXISTS tbl_mark CASCADE;
DROP TABLE IF EXISTS tbl_user CASCADE;

CREATE TABLE tbl_user (
                                   id BIGSERIAL PRIMARY KEY,
                                   login TEXT NOT NULL UNIQUE,
                                   password TEXT NOT NULL,
                                   firstname TEXT NOT NULL,
                                   lastname TEXT NOT NULL
);

CREATE TABLE tbl_issue (
                                    id BIGSERIAL PRIMARY KEY,
                                    user_id BIGINT NOT NULL,
                                    title TEXT NOT NULL,
                                    content TEXT NOT NULL,
                                    created TIMESTAMP NOT NULL,
                                    modified TIMESTAMP NOT NULL,
                                    CONSTRAINT fk_issue_user FOREIGN KEY (user_id)
                                        REFERENCES tbl_user(id)
);

CREATE TABLE tbl_mark (
                                   id BIGSERIAL PRIMARY KEY,
                                   name TEXT NOT NULL
);

CREATE TABLE tbl_issue_mark (
                                         id BIGSERIAL PRIMARY KEY,
                                         issue_id BIGINT NOT NULL,
                                         mark_id BIGINT NOT NULL,
                                         CONSTRAINT fk_issue_mark_issue FOREIGN KEY (issue_id)
                                             REFERENCES tbl_issue(id),
                                         CONSTRAINT fk_issue_mark_mark FOREIGN KEY (mark_id)
                                             REFERENCES tbl_mark(id)
);

INSERT INTO tbl_user (login, password, firstname, lastname)
VALUES ('vladbeskosty7@gmail.com', 'defaultPassword123', 'Владислав', 'Бескостый');
