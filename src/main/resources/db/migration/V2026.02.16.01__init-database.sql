DROP TABLE IF EXISTS em_users;

CREATE TABLE em_users
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    username   VARCHAR(50) NOT NULL,
    user_role  VARCHAR(12)  NOT NULL DEFAULT 'USER',
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL
);

ALTER TABLE em_users
    ADD CONSTRAINT constraint_users_username UNIQUE (username);

ALTER TABLE em_users
    ADD CONSTRAINT constraint_users_email UNIQUE (email);