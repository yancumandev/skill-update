package dev.sorokin.eventmanager.events.api;

import dev.sorokin.eventmanager.events.domain.EventRegistrationService;
import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private final static Logger log = LoggerFactory.getLogger(EventRegistrationController.class);

    private final EventRegistrationService eventRegistrationService;
    private final JwtAuthenticationService authenticationService;
    private final EventDtoMapper eventDtoMapper;

    public EventRegistrationController(EventRegistrationService eventRegistrationService, JwtAuthenticationService authenticationService, EventDtoMapper eventDtoMapper) {
        this.eventRegistrationService = eventRegistrationService;
        this.authenticationService = authenticationService;
        this.eventDtoMapper = eventDtoMapper;
    }


    @PostMapping(path = "/{eventId}")
    public ResponseEntity<Void> registrationOnEvent(@PathVariable Long eventId) {
        log.info("запрос на регистрацию eventId = {}", eventId);

        eventRegistrationService.registrationOnEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @DeleteMapping("cancel/{eventId}")
    public ResponseEntity<Void> cancel(@PathVariable Long eventId) {
        log.info("отмена регистрации eventId = {}", eventId);

        eventRegistrationService.cancel(
                authenticationService.getCurrentAuthenticatedUser(),
                eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserRegistration() {
        log.info("поиск всех регистраций пользователя");

        var foundEvents = eventRegistrationService.getUserRegistration(authenticationService.getCurrentAuthenticatedUser());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundEvents
                        .stream()
                        .map(eventDtoMapper::toDto)
                        .toList()
                );
    }
}
