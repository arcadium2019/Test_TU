package org.example.exo11.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.exo11.model.Priority;

public class CreateTicketRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must have at least 3 characters")
    private String title;

    @NotNull(message = "Priority is required")
    private Priority priority;

    public String getTitle()          { return title; }
    public Priority getPriority()     { return priority; }
    public void setTitle(String t)    { this.title = t; }
    public void setPriority(Priority p) { this.priority = p; }
}
