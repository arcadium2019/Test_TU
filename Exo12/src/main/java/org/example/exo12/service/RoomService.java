package org.example.exo12.service;

import org.example.exo12.exception.RoomNotFoundException;
import org.example.exo12.model.Room;
import org.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room createRoom(String name, int capacity) {
        return repository.save(new Room(name, capacity));
    }

    public Room findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
    }

    public List<Room> findAll() {
        return repository.findAll();
    }
}
