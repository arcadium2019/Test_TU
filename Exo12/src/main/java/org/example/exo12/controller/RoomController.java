package org.example.exo12.controller;

import jakarta.validation.Valid;
import org.example.exo12.dto.CreateRoomRequest;
import org.example.exo12.model.Room;
import org.example.exo12.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@Valid @RequestBody CreateRoomRequest request) {
        return service.createRoom(request.getName(), request.getCapacity());
    }

    @GetMapping
    public List<Room> getAll() {
        return service.findAll();
    }
}
