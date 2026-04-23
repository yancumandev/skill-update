package dev.sorokin.eventmanager.events.db;


import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class EventRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity eventEntity;

    public EventRegistrationEntity() {
    }

    public EventRegistrationEntity(Long id, Long userId, EventEntity eventEntity) {
        this.id = id;
        this.userId = userId;
        this.eventEntity = eventEntity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public EventEntity getEventEntity() {
        return eventEntity;
    }

    public void setEventEntity(EventEntity eventEntity) {
        this.eventEntity = eventEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
