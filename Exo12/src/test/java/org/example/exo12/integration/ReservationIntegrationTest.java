package org.example.exo12.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void full_reservation_lifecycle_create_get_cancel() throws Exception {
        // Step 1 – Create a room
        MvcResult roomResult = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Integration\",\"capacity\":8}"))
                .andExpect(status().isCreated())
                .andReturn();

        String roomId = objectMapper.readTree(roomResult.getResponse().getContentAsString())
                .get("id").asText();

        // Step 2 – Create a reservation
        String body = String.format(
                "{\"roomId\":\"%s\",\"bookedBy\":\"Alice\","
                + "\"startTime\":\"2026-07-01T09:00:00\","
                + "\"endTime\":\"2026-07-01T11:00:00\"}", roomId);

        MvcResult resResult = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn();

        String reservationId = objectMapper.readTree(resResult.getResponse().getContentAsString())
                .get("id").asText();

        // Step 3 – Get the reservation
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookedBy").value("Alice"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        // Step 4 – Cancel the reservation
        mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        // Step 5 – Verify double-cancel returns 409
        mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isConflict());
    }

    @Test
    void overlapping_reservation_returns_409() throws Exception {
        // Create room
        MvcResult roomResult = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Overlap\",\"capacity\":4}"))
                .andExpect(status().isCreated())
                .andReturn();
        String roomId = objectMapper.readTree(roomResult.getResponse().getContentAsString())
                .get("id").asText();

        // First reservation
        String body = String.format(
                "{\"roomId\":\"%s\",\"bookedBy\":\"Bob\","
                + "\"startTime\":\"2026-07-02T10:00:00\","
                + "\"endTime\":\"2026-07-02T12:00:00\"}", roomId);
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated());

        // Overlapping reservation
        String overlap = String.format(
                "{\"roomId\":\"%s\",\"bookedBy\":\"Carol\","
                + "\"startTime\":\"2026-07-02T11:00:00\","
                + "\"endTime\":\"2026-07-02T13:00:00\"}", roomId);
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(overlap))
                .andExpect(status().isConflict());
    }
}
