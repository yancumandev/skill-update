package dev.sorokin.eventmanager.events.api;

import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;


public record EventSearchRequest(
        Integer durationMax,
        LocalDateTime dateStartBefore,
        Integer placesMin,
        Long locationId,
        String eventStatus,
        String name,
        @PositiveOrZero
        Integer placesMax,
        Integer costMin,
        LocalDateTime dateStartAfter,
        Integer costMax,
        Integer durationMin
) {
}
