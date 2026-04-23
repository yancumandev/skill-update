package dev.sorokin.eventmanager.events.api;

import dev.sorokin.eventmanager.events.db.EventEntity;
import dev.sorokin.eventmanager.events.domain.Event;
import org.springframework.stereotype.Component;

@Component
public class EventDtoMapper {

    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.registrationList().size(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.name()
        );
    }

}