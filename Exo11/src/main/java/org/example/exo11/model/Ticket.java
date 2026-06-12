package org.example.exo11.model;

import java.util.UUID;

public class Ticket {
    private final String id;
    private final String title;
    private final Priority priority;
    private TicketStatus status;

    public Ticket(String title, Priority priority) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public String getId()             { return id; }
    public String getTitle()          { return title; }
    public Priority getPriority()     { return priority; }
    public TicketStatus getStatus()   { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
}
