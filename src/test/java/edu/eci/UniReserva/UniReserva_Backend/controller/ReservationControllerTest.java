package edu.eci.UniReserva.UniReserva_Backend.controller;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    public void shouldReturnReservationsForUser() throws Exception {
        String userId = "user123";
        Reservation res1 = new Reservation(userId, "lab1", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 1, false, "Study", null);
        Reservation res2 = new Reservation(userId, "lab2", LocalDate.now().plusDays(1), LocalTime.of(12, 0), LocalTime.of(13, 0), 1, false, "Project", null);

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
    void testUpdateReserve_Success() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenReturn("Reservation updated successfully");

        mockMvc.perform(put("/reservations/update/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation updated successfully"));
    }

    @Test
    void testUpdateReserve_NotFound() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenThrow(new IllegalArgumentException("Reservation with id 123 not found."));

        mockMvc.perform(put("/reservations/update/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reservation with id 123 not found."));
    }

    @Test
    void testUpdateReserve_AlreadyCancelled() throws Exception {
        when(reservationServiceImpl.cancelReservationByReservationId("123"))
                .thenThrow(new IllegalArgumentException("This reservation is already cancelled"));

        mockMvc.perform(put("/reservations/update/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This reservation is already cancelled"));
    }
}
