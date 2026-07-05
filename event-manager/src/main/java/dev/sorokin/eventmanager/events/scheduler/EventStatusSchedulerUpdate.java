package dev.sorokin.eventmanager.events.scheduler;

import dev.sorokin.eventmanager.events.db.EventsRepository;
import dev.sorokin.eventmanager.events.domain.EventStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventStatusSchedulerUpdate {

    private static final Logger log = LoggerFactory.getLogger(EventStatusSchedulerUpdate.class);

    private  final EventsRepository eventsRepository;

    public EventStatusSchedulerUpdate(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    @Scheduled(cron = "${app.scheduling.event-status.cron}")
    public void updateStatusEvent(){
        log.info("EventStatusScheduledUpdater started");

        var startedEvents = eventsRepository.findStartedEventsWithStatus(EventStatus.WAIT_START.name());
        startedEvents.forEach(eventId ->
                eventsRepository.changeEventStatus(eventId, EventStatus.STARTED)
        );

        var endedEvents = eventsRepository.findEndEventsWithStatus(EventStatus.STARTED.name());
        endedEvents.forEach(eventId ->
                eventsRepository.changeEventStatus(eventId, EventStatus.FINISHED)
        );
    }

    }



