package org.example.exo12.repository;

import org.example.exo12.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findById(String id);
    List<Room> findAll();
}
