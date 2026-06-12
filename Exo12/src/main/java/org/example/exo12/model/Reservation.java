package org.example.exo12.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private final String id;
    private final String roomId;
    private final String bookedBy;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private ReservationStatus status;

    public Reservation(String roomId, String bookedBy, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.bookedBy = bookedBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = ReservationStatus.CONFIRMED;
    }

    public String getId()                   { return id; }
    public String getRoomId()               { return roomId; }
    public String getBookedBy()             { return bookedBy; }
    public LocalDateTime getStartTime()     { return startTime; }
    public LocalDateTime getEndTime()       { return endTime; }
    public ReservationStatus getStatus()    { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
