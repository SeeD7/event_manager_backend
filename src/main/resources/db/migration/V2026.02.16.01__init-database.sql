DROP TABLE IF EXISTS em_users;

CREATE TABLE em_users
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(250) NOT NULL,
    last_name  VARCHAR(250) NOT NULL,
    username   VARCHAR(250) NOT NULL,
    user_role  VARCHAR(12)  NOT NULL DEFAULT 'USER',
    email      VARCHAR(250) NOT NULL,
    password   VARCHAR(250) NOT NULL
);