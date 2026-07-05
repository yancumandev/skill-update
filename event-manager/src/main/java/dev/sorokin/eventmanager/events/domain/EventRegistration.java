package dev.sorokin.eventmanager.events.domain;

public record EventRegistration(
        Long id,
        Long user_id,
        Long event
) {
}
