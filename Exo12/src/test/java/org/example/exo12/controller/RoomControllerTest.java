package org.example.exo12.controller;

import org.example.exo12.exception.RoomNotFoundException;
import org.example.exo12.model.Room;
import org.example.exo12.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    void create_valid_room_returns_201() throws Exception {
        // Arrange
        Room room = new Room("Salle Alpha", 10);
        when(roomService.createRoom(any(), anyInt())).thenReturn(room);

        // Act & Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Alpha\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Salle Alpha"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    void create_room_with_blank_name_returns_400() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_room_with_zero_capacity_returns_400() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Beta\",\"capacity\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_all_rooms_returns_200() throws Exception {
        // Arrange
        Room r1 = new Room("A", 5);
        Room r2 = new Room("B", 10);
        when(roomService.findAll()).thenReturn(List.of(r1, r2));

        // Act & Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
