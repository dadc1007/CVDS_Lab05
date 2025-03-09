package edu.eci.UniReserva.UniReserva_Backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.ReservationServiceImpl;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationServiceImpl reservationServiceImpl;

    public ReservationController(ReservationServiceImpl reservationServiceImpl) {
        this.reservationServiceImpl = reservationServiceImpl;
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
            Reservation createdReservation = reservationServiceImpl.createReservation(reservation);
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
        return reservationServiceImpl.getReservationsByUserId(userId);
    }

    /**
     * Endpoint to cancel a reservation.
     *
     * This endpoint should:
     * - Receive the reservation ID from the request path.
     * - Call the service method to cancel the reservation.
     * - Return an appropriate HTTP response.
     *
     * @param reservationId ID of the reservation to cancel.
     * @return ResponseEntity with status 200 if successful, 404 if not found, or 400 if already canceled.
     */
    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<Object> cancelReserve(@PathVariable String reservationId) {
        try{
            Reservation reservation = reservationServiceImpl.cancelReservationByReservationId(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves a list of reservations for a specific date range.
     *
     * @param date1 The start date of the range.
     * @param date2 The end date of the range.
     * @return ResponseEntity with status 200 if successful
     */
    @GetMapping("/range")
    public ResponseEntity<List<Reservation>> getReservationsByRangeDate(@RequestParam String date1, @RequestParam String date2) {
        List<Reservation> response = reservationServiceImpl.getReservationsByRangeDate(date1, date2);
        return ResponseEntity.ok(response);
    }

}
