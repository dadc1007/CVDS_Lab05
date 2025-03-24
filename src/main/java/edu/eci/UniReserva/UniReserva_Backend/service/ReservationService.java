package edu.eci.UniReserva.UniReserva_Backend.service;

import java.util.List;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;

public interface ReservationService {
  ApiResponse<Reservation> createReservation(Reservation reservation);

  ApiResponse<List<Reservation>> getReservationsByUserId(String userId);

  ApiResponse<Reservation> cancelReservationByReservationId(String reservationId);

  ApiResponse<List<Reservation>> getReservationsByRangeDate(String lab, String date1, String date2);

  ApiResponse<List<Reservation>> getAllReservations();
}
