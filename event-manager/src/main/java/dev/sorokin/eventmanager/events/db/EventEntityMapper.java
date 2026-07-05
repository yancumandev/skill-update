package dev.sorokin.eventmanager.events.db;

import dev.sorokin.eventmanager.events.domain.Event;
import dev.sorokin.eventmanager.events.domain.EventRegistration;
import org.springframework.stereotype.Component;

@Component
public class EventEntityMapper {

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getRegistrationList()
                        .stream()
                        .map(x -> new EventRegistration(
                                x.getId(),
                                x.getUserId(),
                                eventEntity.getId()))
                        .toList(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );

    }
}
