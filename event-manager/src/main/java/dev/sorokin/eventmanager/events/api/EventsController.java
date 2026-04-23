package dev.sorokin.eventmanager.events.api;

import dev.sorokin.eventmanager.events.domain.Event;
import dev.sorokin.eventmanager.events.domain.EventsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    private final EventsService eventsService;
    private final EventDtoMapper eventDtoMapper;

    public EventsController(EventsService eventsService, EventDtoMapper eventDtoMapper) {
        this.eventsService = eventsService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @Valid @RequestBody EventRequest eventRequest
    ) {
        log.info("POST {}", eventRequest);
        var createdEvent = eventsService.createEvent(eventRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDtoMapper.toDto(createdEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id
    ) {
        log.info("DEL {}", id);
        eventsService.cancelEvent(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findEventById(
            @PathVariable Long id
    ) {
        log.info("GET {}", id);
        var event = eventsService.findEventById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDtoMapper.toDto(event));
    }


    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest eventRequest
    ) {
        log.info("UPDATE {}", id);
        var updateEvent = eventsService.update(id, eventRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDtoMapper.toDto(updateEvent));
    }


    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> search(
            @Valid @RequestBody EventSearchRequest eventSearchRequest
    ) {
        log.info("SEARCH {}", eventSearchRequest);
        var foundEvents = eventsService.search(eventSearchRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundEvents
                        .stream()
                        .map(eventDtoMapper::toDto)
                        .toList());
    }


    @GetMapping("/my")
    public List<EventDto> findMyEvents() {
        log.info("GET user events ");
        var events = eventsService.searchMyEvents();

        return events.stream()
                .map(eventDtoMapper::toDto)
                .toList();
    }

}
