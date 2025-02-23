package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Creates a new reservation if the lab is available.
     * 
     * @param reservation Object containing the reservation details.
     * @return The created reservation stored in the database.
     * 
     * Possible scenarios:
     * - The reservation is successfully stored if the lab is available.
     * - An exception is thrown if the lab is already booked for the requested time slot.
     */
    public Reservation createReservation(Reservation reservation) {
        return null;
    }

}
