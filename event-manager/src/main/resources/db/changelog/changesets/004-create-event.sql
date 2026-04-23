--liquibase formatted sql
--changeset cuman:004_create_event

CREATE TABLE events
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    owner_id         BIGINT       NOT NULL,
    max_places       INTEGER      NOT NULL,
    occupied_places  INTEGER      NOT NULL DEFAULT 0 CHECK (occupied_places >= 0),
    date_start_at    TIMESTAMP    NOT NULL,
    cost             INTEGER      NOT NULL CHECK (cost >= 1),
    duration_minutes INTEGER      NOT NULL CHECK (duration_minutes >= 30),
    location_id      BIGINT       NOT NULL,
    status           VARCHAR(50)  NOT NULL
);

