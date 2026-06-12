package org.example.exo12.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateReservationRequest {

    @NotBlank(message = "Room id is required")
    private String roomId;

    @NotBlank(message = "Booker name is required")
    private String bookedBy;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public String getRoomId()               { return roomId; }
    public String getBookedBy()             { return bookedBy; }
    public LocalDateTime getStartTime()     { return startTime; }
    public LocalDateTime getEndTime()       { return endTime; }
    public void setRoomId(String r)         { this.roomId = r; }
    public void setBookedBy(String b)       { this.bookedBy = b; }
    public void setStartTime(LocalDateTime s) { this.startTime = s; }
    public void setEndTime(LocalDateTime e)   { this.endTime = e; }
}
