package org.example.exo12.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    public String getName()         { return name; }
    public int getCapacity()        { return capacity; }
    public void setName(String n)   { this.name = n; }
    public void setCapacity(int c)  { this.capacity = c; }
}
