package edu.eci.UniReserva.UniReserva_Backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Object> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * Retrieves a list of reservations for a specific user.
     *
     * @param userId The unique identifier of the user whose reservations are to be retrieved.
     * @return A list of {@link Reservation} objects associated with the given user.
     */
    @GetMapping("/user/{userId}")
    public List<Reservation> getUserReservations(@PathVariable String userId) {
        return reservationService.getReservationsByUserId(userId);
    }


    
}
