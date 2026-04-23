package dev.sorokin.eventmanager.events.api;

import dev.sorokin.eventmanager.events.db.EventsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventsController {

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    private final EventsService eventsService;


    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @PostMapping
    public EventDto createEvent(@Valid @RequestBody EventRequest eventRequest) {
        log.info("POST {}", eventRequest);
        eventsService.createEvent(eventRequest);
        return null;
    }

}
