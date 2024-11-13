package com.example.authsystem.Entities;

import jakarta.persistence.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "etag", nullable = false)
    private UUID etag;

    @Column(name = "status", nullable = false)
    private short status = 3;

    @Column(name = "date_of_recorded", nullable = false, updatable = false)
    private Timestamp dateOfRecorded;

    @Column(name = "user_who_recorded", nullable = false, length = 50, updatable = false)
    private String userWhoRecorded;

    @Column(name = "date_of_last_updated", nullable = false)
    private Timestamp dateOfLastUpdated;

    @Column(name = "user_who_last_updated", nullable = false, length = 50)
    private String userWhoLastUpdated;

    @Column(name = "counter_of_unique_data", nullable = false)
    private long counterOfUniqueData;

    @PrePersist
    protected void onCreate() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        this.dateOfRecorded = new Timestamp(System.currentTimeMillis());
        this.userWhoRecorded = currentUser;
        this.dateOfLastUpdated = new Timestamp(System.currentTimeMillis());
        this.userWhoLastUpdated = currentUser;
        this.etag = UUID.randomUUID();
        this.counterOfUniqueData = (long) (Math.random() * Long.MAX_VALUE) + 1;
    }

    @PreUpdate
    protected void onUpdate() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        this.dateOfLastUpdated = new Timestamp(System.currentTimeMillis());
        this.userWhoLastUpdated = currentUser;
        this.etag = UUID.randomUUID();
        this.counterOfUniqueData = (long) (Math.random() * Long.MAX_VALUE) + 1;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEtag() {
        return etag;
    }

    public void setEtag(UUID etag) {
        this.etag = etag;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Timestamp getDateOfRecorded() {
        return dateOfRecorded;
    }

    public String getUserWhoRecorded() {
        return userWhoRecorded;
    }

    public Timestamp getDateOfLastUpdated() {
        return dateOfLastUpdated;
    }

    public String getUserWhoLastUpdated() {
        return userWhoLastUpdated;
    }

    public long getCounterOfUniqueData() {
        return counterOfUniqueData;
    }

    public void setCounterOfUniqueData(long counterOfUniqueData) {
        this.counterOfUniqueData = counterOfUniqueData;
    }
}