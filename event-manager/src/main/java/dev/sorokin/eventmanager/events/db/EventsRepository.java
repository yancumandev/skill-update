package dev.sorokin.eventmanager.events.db;

import dev.sorokin.eventmanager.events.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<EventEntity, Long> {

    @Query(value = """
            SELECT * FROM  events
              where (:durationMax IS NULL OR events.duration_minutes<= :durationMax)
              and (CAST(:dateStartBefore as timestamp) is null or events.date_start_at <=:dateStartBefore)
              and (:placesMin is null or events.max_places >=:placesMin)
              and (:locationId is null or events.location_id =:locationId)
              and (:eventStatus is null or events.status =:eventStatus)
              and (:name is null or events.name =:name)
              and (:placesMax is null or events.max_places <=:placesMax)
              and (:costMin is null or events.cost >=:costMin)
              and (CAST(:dateStartBefore as timestamp)  is null or events.date_start_at >=:dateStartAfter)
              and (:costMax is null or events.cost <=:costMax)
              and (:durationMin is null or events.duration_minutes >=:durationMin)
            """, nativeQuery = true)
    List<EventEntity> searchByFilter(
            @Param("durationMax") Integer durationMax,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("placesMin") Integer placesMin,
            @Param("locationId") Long locationId,
            @Param("eventStatus") String eventStatus,
            @Param("name") String name,
            @Param("placesMax") Integer placesMax,
            @Param("costMin") Integer costMin,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("costMax") Integer costMax,
            @Param("durationMin") Integer durationMin
    );

    @Query("""
        SELECT ev
        FROM EventEntity ev
        LEFT JOIN FETCH ev.registrationList
        WHERE ev.id = :id
        """)
    Optional<EventEntity> findByIdWithRegistrations(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT ev
            FROM EventEntity ev
            LEFT JOIN FETCH ev.registrationList
            WHERE ev.ownerId = :user_id
            """)
    List<EventEntity> searchMyEvents(@Param("user_id") Long id);

    @Query(value = """
            SELECT id
            FROM events
            WHERE status = :status
            AND date_start_at < CURRENT_TIMESTAMP
            """, nativeQuery = true)
    List<Long> findStartedEventsWithStatus(@Param("status") String eventStatus);

    @Query(value = """
            SELECT id
            FROM events e
            WHERE e.status = :status
            AND e.date_start_at + INTERVAL '1 minute' * e.duration_minutes < NOW()
            """, nativeQuery = true)
    List<Long> findEndEventsWithStatus(@Param("status") String eventStatus);


    @Modifying
    @Transactional
    @Query("update EventEntity e set e.status = :status where e.id = :id")
    void changeEventStatus(
            @Param("id") Long eventId,
            @Param("status") EventStatus status
    );


}
