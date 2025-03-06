package edu.eci.UniReserva.UniReserva_Backend.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.ReservationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationServiceImpl reservationServiceImpl;

    @InjectMocks
    private ReservationController reservationController;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

    }

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Reservation testReservation = new Reservation(
                "user123",
                "lab01",
                "2025-05-01",
                "10:00",
                "12:00",
                "Project research"
        );


        when(reservationServiceImpl.createReservation(any(Reservation.class))).thenReturn(testReservation);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.labId").value("lab01"))
                .andExpect(jsonPath("$.date").value("2025-05-01"))
                .andExpect(jsonPath("$.startTime").value("10:00"))
                .andExpect(jsonPath("$.endTime").value("12:00"))
                .andExpect(jsonPath("$.purpose").value("Project research"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }


    @Test
    public void shouldReturnReservationsForUser() throws Exception {
        String userId = "user123";
        String date1 = LocalDate.now().format(dateFormatter);
        String date2 = LocalDate.now().plusDays(1).format(dateFormatter);
        Reservation res1 = new Reservation(userId, "lab1", date1, "10:00", "11:00", "Study");
        Reservation res2 = new Reservation(userId, "lab2", date2, "12:00", "13:00", "Project");

        when(reservationServiceImpl.getReservationsByUserId(userId)).thenReturn(Arrays.asList(res1, res2));

        mockMvc.perform(get("/reservations/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].labId").value("lab1"))
                .andExpect(jsonPath("$[1].labId").value("lab2"));

        verify(reservationServiceImpl, times(1)).getReservationsByUserId(userId);
    }


    @Test
    public void shouldReturnEmptyListForUserWithoutReservations() throws Exception {
        String userId = "user456";

        when(reservationServiceImpl.getReservationsByUserId(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reservations/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(reservationServiceImpl, times(1)).getReservationsByUserId(userId);
    }

    @Test
    void shouldCancelAReservation() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenReturn("Reservation canceled successfully");

        mockMvc.perform(put("/reservations/cancel/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation canceled successfully"));
    }

    @Test
    void shouldNotCancelAReservationWhenItNotExist() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenThrow(new IllegalArgumentException("Reservation with id 123 not found."));

        mockMvc.perform(put("/reservations/cancel/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reservation with id 123 not found."));
    }

    @Test
    void shouldNotCancelAReservationWhenAlreadyIsCancelled() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenThrow(new IllegalArgumentException("This reservation is already cancelled"));

        mockMvc.perform(put("/reservations/cancel/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This reservation is already cancelled"));
    }
}
