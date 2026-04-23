--liquibase formatted sql
--changeset cuman:005_drop_occupied_places

ALTER TABLE events
DROP COLUMN occupied_places;