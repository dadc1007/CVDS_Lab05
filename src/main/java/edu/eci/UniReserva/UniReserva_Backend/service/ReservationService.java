package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;

import java.time.LocalTime;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    public boolean isLabAvailable(String labId, LocalTime startTime, LocalTime endTime);
    public List<Reservation> getReservationsByUserId(String userId);
    public String  updateReservationByReservationId(String reservationId);
}
