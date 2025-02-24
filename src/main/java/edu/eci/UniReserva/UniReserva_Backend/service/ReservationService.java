package edu.eci.UniReserva.UniReserva_Backend.service;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
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
        if (!reservation.getStartTime().isBefore(reservation.getEndTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        
        if (reservation.getId() != null && reservationRepository.findById(reservation.getId()).isPresent()) {
            throw new IllegalArgumentException("A reservation with this ID already exists.");
        }
        
        if (!isLabAvailable(reservation.getLabId(), reservation.getStartTime(), reservation.getEndTime())) 
            throw new IllegalArgumentException("The lab is not available for the requested time slot.");
        
        if (reservationRepository.findByLabAndTime(reservation.getLabId(), reservation.getStartTime(), reservation.getEndTime()).isPresent()) {
            throw new IllegalArgumentException("A reservation for this lab and time slot already exists.");
        }
        
        return reservationRepository.save(reservation);
    }
    

    public boolean isLabAvailable(String labId, LocalTime startTime, LocalTime endTime) {
        return reservationRepository.findConflictingReservations(labId, startTime, endTime).isEmpty();
    }


    /**
     * Retrieves a list of reservations for a specific user, sorted by date and start time.
     *
     * @param userId the unique identifier of the user whose reservations are being fetched
     * @return a list of {@link Reservation} objects belonging to the specified user,
     *         sorted in ascending order by date and then by start time
     */
    public List<Reservation> getReservationsByUserId(String userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Reservation::getDate)
                        .thenComparing(Reservation::getStartTime))
                .collect(Collectors.toList());
    }

    public String  updateReservationByReservationId(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if(reservation == null) throw new IllegalArgumentException("Rerservation with id " + reservationId + " not found.");
        if(reservation.getStatus().equals(ReservationStatus.CANCELED)) throw new IllegalArgumentException("This reservation is already cancelled");

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        return "Reservation updated successfully";
    }

}
