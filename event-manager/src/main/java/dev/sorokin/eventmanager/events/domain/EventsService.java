package dev.sorokin.eventmanager.events.domain;


import dev.sorokin.eventmanager.events.api.EventRequest;
import dev.sorokin.eventmanager.events.api.EventSearchRequest;
import dev.sorokin.eventmanager.events.db.EventEntity;
import dev.sorokin.eventmanager.events.db.EventEntityMapper;
import dev.sorokin.eventmanager.events.db.EventsRepository;
import dev.sorokin.eventmanager.locations.LocationService;
import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventsService {

    private final static Logger log = LoggerFactory.getLogger(EventsService.class);

    private final EventsRepository eventsRepository;
    private final LocationService locationService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final EventEntityMapper eventEntityMapper;


    public EventsService(EventsRepository eventsRepository, LocationService locationService, JwtAuthenticationService jwtAuthenticationService, EventEntityMapper eventEntityMapper) {
        this.eventsRepository = eventsRepository;
        this.locationService = locationService;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.eventEntityMapper = eventEntityMapper;
    }

    public Event createEvent(EventRequest eventRequest) {

        var location = locationService.findById(eventRequest.locationId());
        if (location.capacity() < eventRequest.maxPlaces()) {
            throw new IllegalArgumentException("В данной локации вместимость всего %s человек. Вы указали %s".formatted(location.capacity(), eventRequest.maxPlaces()));
        }

        var user = jwtAuthenticationService.getCurrentAuthenticatedUser();

        var newEvent = new EventEntity(
                null,
                eventRequest.name(),
                user.id(),
                eventRequest.maxPlaces(),
                List.of(),
                eventRequest.date(),
                eventRequest.cost(),
                eventRequest.duration(),
                eventRequest.locationId(),
                EventStatus.WAIT_START
        );

        EventEntity eventEntity = eventsRepository.save(newEvent);
        return eventEntityMapper.toDomain(eventEntity);
    }


    public void cancelEvent(Long id) {
        var foundEvent = findEvent(id);
        var user = jwtAuthenticationService.getCurrentAuthenticatedUser();
        if (!user.role().name().equals("ADMIN") && !user.id().equals(foundEvent.getOwnerId())) {
            throw new IllegalArgumentException("У вас нет прав доступа. Доступно только владельцу или администратору");
        }
        if (!foundEvent.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Невозможно отменить так как уже начато");
        }
        foundEvent.setStatus(EventStatus.CANCELLED);
        eventsRepository.save(foundEvent);
    }


    public EventEntity findEvent(Long id) {
        var foundEvent = eventsRepository.findByIdWithRegistrations(id).orElseThrow(()
                -> new EntityNotFoundException("Сущность с Id =%d не найдена ".formatted(id)));
        return foundEvent;
    }


    public Event findEventById(Long id) {
        var foundEvent = findEvent(id);
        return eventEntityMapper.toDomain(foundEvent);
    }

    @Transactional
    public Event update(Long id, EventRequest eventUpdate) {
        var foundEvent = findEvent(id);// сначала проверка существует ли встреча

        var location = locationService.findById(eventUpdate.locationId()); // затем существует ли айди локации которую обновляем

        if (!foundEvent.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Уже началось, отменить нельзя");
        }

        if (location.capacity() < eventUpdate.maxPlaces()) {

            throw new IllegalArgumentException("недостаточно места в этой локации");
        }

        var user = jwtAuthenticationService.getCurrentAuthenticatedUser();

        if (!user.id().equals(foundEvent.getOwnerId()) && !user.role().name().equals("ADMIN")) {
            throw new IllegalArgumentException("У вас нет прав доступа. Доступно только владельцу или администратору");
        }

        foundEvent.setName(eventUpdate.name());
        foundEvent.setMaxPlaces(eventUpdate.maxPlaces());
        foundEvent.setDate(eventUpdate.date());
        foundEvent.setCost(eventUpdate.cost());
        foundEvent.setDuration(eventUpdate.duration());
        foundEvent.setLocationId(eventUpdate.locationId());
        var savedEvent = eventsRepository.save(foundEvent);

        return eventEntityMapper.toDomain(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<Event> search(EventSearchRequest eventSearchRequest) {
        List<EventEntity> events = eventsRepository.searchByFilter(
                eventSearchRequest.durationMax(),
                eventSearchRequest.dateStartBefore(),
                eventSearchRequest.placesMin(),
                eventSearchRequest.locationId(),
                eventSearchRequest.eventStatus(),
                eventSearchRequest.name(),
                eventSearchRequest.placesMax(),
                eventSearchRequest.costMin(),
                eventSearchRequest.dateStartAfter(),
                eventSearchRequest.costMax(),
                eventSearchRequest.durationMin()
        );

        return events.stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }


    public List<Event> searchMyEvents() {
        var user = jwtAuthenticationService.getCurrentAuthenticatedUser();

        List<EventEntity> result = eventsRepository.searchMyEvents(user.id());

        return result.stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }
}
