package dev.sorokin.eventmanager.events.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

//@Entity
//@Table(name = "")
public class EventEntity {
    @Id
    private Long id;
    @Column(name = "date",nullable = false)
    private LocalDate date;
    @Column(name = "duration",nullable = false)
    private Integer duration;
    @Column(name = "cost",nullable = false)
    private Integer cost;
    @Column(name = "maxPlaces", nullable = false)
    private Integer maxPlaces;
    @Column(name = "locationId",nullable = false)
    private Integer locationId;
    @Column(name = "name", nullable = false)
    private String name;

    public EventEntity() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
