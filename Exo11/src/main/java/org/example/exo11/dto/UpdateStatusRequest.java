package org.example.exo11.dto;

import jakarta.validation.constraints.NotNull;
import org.example.exo11.model.TicketStatus;

public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private TicketStatus status;

    public TicketStatus getStatus()          { return status; }
    public void setStatus(TicketStatus s)    { this.status = s; }
}
