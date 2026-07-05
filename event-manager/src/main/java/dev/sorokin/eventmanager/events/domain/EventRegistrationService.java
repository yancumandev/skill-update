package dev.sorokin.eventmanager.events.domain;

import dev.sorokin.eventmanager.events.db.EventEntityMapper;
import dev.sorokin.eventmanager.events.db.EventRegistrationEntity;
import dev.sorokin.eventmanager.events.db.EventRegistrationRepository;
import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    private final static Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    private final EventRegistrationRepository eventRegistrationRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final EventsService eventsService;
    private final EventEntityMapper eventEntityMapper;


    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository, JwtAuthenticationService jwtAuthenticationService, EventsService eventsService, EventEntityMapper eventEntityMapper) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.eventsService = eventsService;
        this.eventEntityMapper = eventEntityMapper;
    }


    @Transactional
    public void registrationOnEvent(Long eventId) {
        var user = jwtAuthenticationService.getCurrentAuthenticatedUser();
        var event = eventsService.findEvent(eventId);

        log.info("достали событие");
        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("событие уже началось");
        }

        log.info("проверка на владельца");
        if (event.getOwnerId().equals(user.id())) {
            throw new IllegalArgumentException(" ты владелец события и не можешь сюда быть записан");
        }

        log.info("проверка записан или нет");
        var registration = eventRegistrationRepository.findEventRegistration(user.id(), event.getId());
        if (registration.isPresent()) {
            throw new IllegalArgumentException("уже записан на мероприятие");
        }

        log.info("проверка места");
        Long result = eventRegistrationRepository.findSizeEvent(event.getId());
        if (event.getMaxPlaces() <= result) {
            throw new IllegalArgumentException("Все места заняты");
        }

        EventRegistrationEntity eventRegistrationEntity = new EventRegistrationEntity(
                null,
                user.id(),
                event
        );

        eventRegistrationRepository.save(eventRegistrationEntity);
    }


    public void cancel(
            User user,
            Long eventId
    ) {
        var event = eventsService.findEvent(eventId);
        var result = eventRegistrationRepository.findEventRegistration(user.id(), event.getId());
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Регистрации не найдено");
        }
        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(" Нельзя отменить потому что событие %s".formatted(event.getStatus()));
        }

        eventRegistrationRepository.delete(result.orElseThrow());
    }


    public List<Event> getUserRegistration(User user) {

        var foundEvents = eventRegistrationRepository.getUserRegistration(user.id());

        return foundEvents
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }
}
