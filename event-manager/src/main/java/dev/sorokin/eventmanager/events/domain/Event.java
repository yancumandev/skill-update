package dev.sorokin.eventmanager.events.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        List<EventRegistration> registrationList,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Long locationId,
        EventStatus status
) {
}
