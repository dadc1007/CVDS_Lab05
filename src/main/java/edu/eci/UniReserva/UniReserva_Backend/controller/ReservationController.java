package edu.eci.UniReserva.UniReserva_Backend.controller;

import java.util.List;
import java.util.Map;

import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
  private final ReservationService reservationService;

  /**
   * Creates a new reservation.
   *
   * @param reservation Object containing the reservation details.
   * @return ResponseEntity with the operation result.
   *     <p>Possible responses: - 200 OK: The reservation was successfully created. - 400 BAD
   *     REQUEST: The reservation could not be created due to invalid data or availability
   *     conflicts.
   */
  @PostMapping
  public ResponseEntity<ApiResponse<Reservation>> createReservation(
      @RequestBody Reservation reservation) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(reservationService.createReservation(reservation));
  }

  /**
   * Retrieves a list of reservations for a specific user.
   *
   * @param userId The unique identifier of the user whose reservations are to be retrieved.
   * @return A list of {@link Reservation} objects associated with the given user.
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<List<Reservation>>> getUserReservations(
      @PathVariable String userId) {
    return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
  }

  /**
   * Endpoint to cancel a reservation.
   *
   * <p>This endpoint should: - Receive the reservation ID from the request path. - Call the service
   * method to cancel the reservation. - Return an appropriate HTTP response.
   *
   * @param reservationId ID of the reservation to cancel.
   * @return ResponseEntity with status 200 if successful, 404 if not found, or 400 if already
   *     canceled.
   */
  @PutMapping("/cancel/{reservationId}")
  public ResponseEntity<ApiResponse<Reservation>> cancelReserve(
      @PathVariable String reservationId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reservationService.cancelReservationByReservationId(reservationId));
  }

  /**
   * Retrieves a list of reservations for a specific date range.
   *
   * @param lab The lab to consult.
   * @param date1 The start date of the range.
   * @param date2 The end date of the range.
   * @return ResponseEntity with status 200 if successful
   */
  @GetMapping("/range")
  public ResponseEntity<ApiResponse<List<Reservation>>> getReservationsByRangeDate(
      @RequestParam String lab, @RequestParam String date1, @RequestParam String date2) {
    return ResponseEntity.ok(reservationService.getReservationsByRangeDate(lab, date1, date2));
  }
}
