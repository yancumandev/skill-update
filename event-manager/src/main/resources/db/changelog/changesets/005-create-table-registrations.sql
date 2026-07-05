--liquibase formatted sql
--changeset cuman:005-create-table-registrations

CREATE TABLE registrations
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT NOT NULL,
    event_id BIGINT NOT NULL,

    CONSTRAINT fk_registrations_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_registrations_user FOREIGN KEY (user_id) REFERENCES users (id)
);

