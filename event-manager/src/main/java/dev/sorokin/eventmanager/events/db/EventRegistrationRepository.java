package dev.sorokin.eventmanager.events.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {


    @Query(value = "select COUNT(*) from registrations WHERE event_id=:id_event ", nativeQuery = true)
    Long findSizeEvent(@Param("id_event") Long id);


    @Query(value = "select * from registrations WHERE user_id=:id AND event_id=:id1", nativeQuery = true)
    Optional<EventRegistrationEntity> findEventRegistration(
            @Param("id") Long id,
            @Param("id1") Long id1);


    @Query("""
            SELECT DISTINCT reg.eventEntity
            FROM EventRegistrationEntity reg
            LEFT JOIN FETCH reg.eventEntity.registrationList
            WHERE reg.userId = :userId
            """)
    List<EventEntity> getUserRegistration(@Param("userId") Long userId);

}
