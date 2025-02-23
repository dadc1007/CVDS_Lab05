package edu.eci.UniReserva.UniReserva_Backend.controller;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Creates a new reservation.
     * 
     * @param reservation Object containing the reservation details.
     * @return ResponseEntity with the operation result.
     * 
     * Possible responses:
     * - 200 OK: The reservation was successfully created.
     * - 400 BAD REQUEST: The reservation could not be created due to invalid data or availability conflicts.
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        return null;
    }

    public boolean isLabAvailable(String labId, LocalDateTime startTime, LocalDateTime endTime) {
        return false;
    }
}
