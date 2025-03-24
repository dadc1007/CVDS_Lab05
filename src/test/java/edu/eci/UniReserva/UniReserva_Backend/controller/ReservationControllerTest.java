package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ReservationService reservationService;

  @Autowired private ObjectMapper objectMapper;

  private Reservation reservation1;
  private Reservation reservation2;

  @BeforeEach
  void setUp() {
    reservation1 = new Reservation();
    reservation1.setId("1");
    reservation1.setUserId("123");

    reservation2 = new Reservation();
    reservation2.setId("2");
    reservation2.setUserId("456");
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldCreateReservation() throws Exception {
    ApiResponse<Reservation> mockResponse =
        ApiResponse.<Reservation>builder()
            .status("success")
            .message("Reservation created")
            .data(reservation1)
            .build();

    when(reservationService.createReservation(ArgumentMatchers.any(Reservation.class)))
        .thenReturn(mockResponse);

    mockMvc
        .perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation1)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Reservation created"))
        .andExpect(jsonPath("$.data.id").value("1"));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldGetUserReservations() throws Exception {
    List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
    ApiResponse<List<Reservation>> mockResponse =
        ApiResponse.<List<Reservation>>builder()
            .status("success")
            .message("Reservations retrieved")
            .data(reservations)
            .build();

    when(reservationService.getReservationsByUserId("123")).thenReturn(mockResponse);

    mockMvc
        .perform(get("/reservations/user/123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.data.length()").value(2));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldCancelReservation() throws Exception {
    ApiResponse<Reservation> mockResponse =
        ApiResponse.<Reservation>builder()
            .status("success")
            .message("Reservation canceled")
            .data(reservation1)
            .build();

    when(reservationService.cancelReservationByReservationId("1")).thenReturn(mockResponse);

    mockMvc
        .perform(put("/reservations/cancel/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Reservation canceled"));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldGetReservationsByRangeDate() throws Exception {
    List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
    ApiResponse<List<Reservation>> mockResponse =
        ApiResponse.<List<Reservation>>builder()
            .status("success")
            .message("Reservations retrieved")
            .data(reservations)
            .build();

    when(reservationService.getReservationsByRangeDate("Lab1", "2024-03-01", "2024-03-10"))
        .thenReturn(mockResponse);

    mockMvc
        .perform(
            get("/reservations/range")
                .param("lab", "Lab1")
                .param("date1", "2024-03-01")
                .param("date2", "2024-03-10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.data.length()").value(2));
  }
}
