--liquibase formatted sql
--changeset cuman:003_create_users

CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(500) NOT NULL,
    age      INTEGER NOT NULL CHECK (age >= 18),
    role     VARCHAR(500) NOT NULL
);

--rollback DROP TABLE users;