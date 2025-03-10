package edu.eci.UniReserva.UniReserva_Backend.service;

import java.util.List;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    List<Reservation> getReservationsByUserId(String userId);
    Reservation cancelReservationByReservationId(String reservationId);
    List<Reservation> getReservationsByRangeDate(String lab, String date1, String date2);
}
