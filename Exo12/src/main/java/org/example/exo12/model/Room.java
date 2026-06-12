package org.example.exo12.model;

import java.util.UUID;

public class Room {
    private final String id;
    private final String name;
    private final int capacity;

    public Room(String name, int capacity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.capacity = capacity;
    }

    public String getId()       { return id; }
    public String getName()     { return name; }
    public int getCapacity()    { return capacity; }
}
