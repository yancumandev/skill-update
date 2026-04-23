package dev.sorokin.eventmanager.events.db;

import dev.sorokin.eventmanager.events.api.EventRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    private final static Logger log = LoggerFactory.getLogger(EventsService.class);
//
//    private final EventsRepository eventsRepository;
//
//    public EventsService(EventsRepository eventsRepository) {
//        this.eventsRepository = eventsRepository;
//    }

    public void createEvent(EventRequest eventRequest) {
        // должен добавить
        //  occupied Places   занимаемые места
        // тело запроса
        //  owner Id   Идентификатор владельца

        // + енам


    }


}
